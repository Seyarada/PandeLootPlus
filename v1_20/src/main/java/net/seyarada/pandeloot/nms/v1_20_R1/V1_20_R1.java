package net.seyarada.pandeloot.nms.v1_20_R1;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.seyarada.pandeloot.nms.NMSMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class V1_20_R1 implements NMSMethods {

    @Override
    public List<Entity> hologram(int duration, Location location, Player player, List<String> text, JavaPlugin plugin) {
        final ServerLevel wS = ((CraftWorld) location.getWorld()).getHandle();
        double lX = location.getX();
        double lY = location.getY() + 1.2;
        double lZ = location.getZ();



        List<Display.TextDisplay> holograms = new ArrayList<>();

        Collections.reverse(text);

        for (String msg : text) {
            if(msg == null) continue;
            if(msg.isEmpty()) {
                lY += 0.22;
                holograms.add(null);
                continue;
            }
            lY += 0.22;

            final Display.TextDisplay displayEntity = new Display.TextDisplay(EntityType.TEXT_DISPLAY, wS);
            displayEntity.setPos(lX, lY, lZ);
            displayEntity.setCustomName(Component.literal(msg));
            displayEntity.setCustomNameVisible(true);
            displayEntity.setInterpolationDuration(20);
            displayEntity.setInterpolationDelay(0);
            holograms.add(displayEntity);
            int id = displayEntity.getId();
            ClientboundAddEntityPacket packetPlayOutSpawnEntity = new ClientboundAddEntityPacket(displayEntity, 1);
            ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(id, displayEntity.getEntityData().packDirty());

            final ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(packetPlayOutSpawnEntity);
            connection.send(metadata);

            if(duration>0) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        ClientboundRemoveEntitiesPacket destroy = new ClientboundRemoveEntitiesPacket(id);
                        connection.send(destroy);
                    }
                }, duration);
            }
        }
        return holograms.stream().filter(Objects::nonNull).map(s -> (Entity) s.getBukkitEntity()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void destroyEntity(int toBeDestroyed, Entity player) {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(toBeDestroyed);
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    @Override
    public void displayToast(Player player, String title, String frame, org.bukkit.inventory.ItemStack icon) {
        ResourceLocation minecraftKey = new ResourceLocation("pandeloot", "notification");
        HashMap<String, Criterion> criteria = new HashMap<>();

        criteria.put("for_free", new Criterion(new CriterionTriggerInstance() {
            public ResourceLocation getCriterion() {
                return new ResourceLocation("minecraft", "impossible");
            }

            @Override  // Not needed
            public JsonObject serializeToJson(SerializationContext serializationContext) {
                return null;
            }
        }));

        ArrayList<String[]> fixed = new ArrayList<>();
        fixed.add(new String[]{"for_free"});

        String[][] requirements = fixed.toArray(new String[fixed.size()][]);

        MutableComponent chatTitle = Component.translatable(title);
        //IChatBaseComponent chatDescription = new ChatMessage(description);
        FrameType advancementFrame = FrameType.valueOf(frame.toUpperCase());
        net.minecraft.world.item.ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);

        DisplayInfo display = new DisplayInfo(craftIcon, chatTitle, null, null, advancementFrame, true, true, true);
        AdvancementRewards reward = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], null);
        Advancement advancement = new Advancement(minecraftKey, null, display, reward, criteria, requirements, false);

        HashMap<ResourceLocation, AdvancementProgress> progressMap = new HashMap<>();
        AdvancementProgress progress = new AdvancementProgress();
        progress.update(criteria, requirements);
        progress.getCriterion("for_free").grant(); // NMS: Criterion progress
        progressMap.put(minecraftKey, progress);

        ClientboundUpdateAdvancementsPacket packet
                = new ClientboundUpdateAdvancementsPacket(false, Collections.singletonList(advancement), new HashSet<>(), progressMap);
        ((CraftPlayer) player).getHandle().connection.send(packet);

        // Remove the advancement
        HashSet<ResourceLocation> remove = new HashSet<>();
        remove.add(minecraftKey);
        progressMap.clear();
        packet = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>(), remove, progressMap);
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    @Override
    public void injectPlayer(Player player) {
        ServerPlayer ply = ((CraftPlayer) player).getHandle();
        ChannelHandler_V1_20_R1 cdh = new ChannelHandler_V1_20_R1(ply);

        ChannelPipeline pipeline = getConnectionWithReflection(player).channel.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof net.minecraft.network.Connection) {
                pipeline.addBefore(name, "pande_loot_packet_handler", cdh);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        Channel channel = getConnectionWithReflection(player).channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("pande_loot_packet_handler");
            return null;
        });
    }

    @Override
    public void updateHologramPosition(double x, double y, double z, Entity hologram, Player player) {
        if (player == null) return;
        Display.TextDisplay stand = ((CraftTextDisplay) hologram).getHandle();
        stand.setPos(x, y, z);

        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        connection.send(new ClientboundTeleportEntityPacket(stand));
    }

    @Override
    public ItemStack getCustomTextureHead(ItemStack head, String value) {
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        if(value==null) {
            meta.setOwner("PandemoniumHK");
            head.setItemMeta(meta);
            return head;
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

    net.minecraft.network.Connection getConnectionWithReflection(Player player) {
        // Why did Nokanj made this variable private ??
        try {
            // "h" is the "connection" variable
            Field connectionField = ((CraftPlayer) player).getHandle().connection.getClass().getDeclaredField("h");
            connectionField.setAccessible(true);
            return (net.minecraft.network.Connection) connectionField.get(((CraftPlayer) player).getHandle().connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
