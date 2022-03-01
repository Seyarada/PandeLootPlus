package net.seyarada.pandeloot.nms.v1_18_R2;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.seyarada.pandeloot.nms.NMSMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class V1_18_R2 implements NMSMethods {

    @Override
    public List<Entity> hologram(int duration, Location location, Player player, List<String> text, JavaPlugin plugin) {
        final ServerLevel wS = ((CraftWorld) location.getWorld()).getHandle();
        double lX = location.getX();
        double lY = location.getY() + 1.2;
        double lZ = location.getZ();

        List<ArmorStand> armorStands = new ArrayList<>();

        Collections.reverse(text);

        for (String msg : text) {
            if(msg == null) continue;
            if(msg.isEmpty()) {
                lY += 0.22;
                armorStands.add(null);
                continue;
            }
            lY += 0.22;

            final ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, wS);
            armorStands.add(armorStand);
            //ArmorStand bArmorStand = (((ArmorStand) armorStand.getBukkitEntity()));
            int id = armorStand.getId();
            armorStand.setPos(lX, lY, lZ);
            armorStand.setCustomName(new TranslatableComponent(msg));
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);
            armorStand.setMarker(true);
            armorStand.setRemainingFireTicks(Integer.MAX_VALUE);
            ClientboundAddEntityPacket packetPlayOutSpawnEntity = new ClientboundAddEntityPacket(armorStand, 1);
            ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(id, armorStand.getEntityData(), true);

            final Connection connection = ((CraftPlayer) player).getHandle().connection.getConnection();
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
        return armorStands.stream().filter(Objects::nonNull).map(s -> (Entity) s.getBukkitEntity()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void destroyEntity(int toBeDestroyed, Entity player) {
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(toBeDestroyed);
        ((CraftPlayer) player).getHandle().connection.getConnection().send(packet);
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

        TranslatableComponent chatTitle = new TranslatableComponent(title);
        //IChatBaseComponent chatDescription = new ChatMessage(description);
        FrameType advancementFrame = FrameType.valueOf(frame.toUpperCase());
        net.minecraft.world.item.ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);

        DisplayInfo display = new DisplayInfo(craftIcon, chatTitle, null, null, advancementFrame, true, true, true);
        AdvancementRewards reward = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], null);
        Advancement advancement = new Advancement(minecraftKey, null, display, reward, criteria, requirements);

        HashMap<ResourceLocation, AdvancementProgress> progressMap = new HashMap<>();
        AdvancementProgress progress = new AdvancementProgress();
        progress.update(criteria, requirements);
        progress.getCriterion("for_free").grant(); // NMS: Criterion progress
        progressMap.put(minecraftKey, progress);

        ClientboundUpdateAdvancementsPacket packet
                = new ClientboundUpdateAdvancementsPacket(false, Collections.singletonList(advancement), new HashSet<>(), progressMap);
        ((CraftPlayer) player).getHandle().connection.getConnection().send(packet);

        // Remove the advancement
        HashSet<ResourceLocation> remove = new HashSet<>();
        remove.add(minecraftKey);
        progressMap.clear();
        packet = new ClientboundUpdateAdvancementsPacket(false, new ArrayList<>(), remove, progressMap);
        ((CraftPlayer) player).getHandle().connection.getConnection().send(packet);
    }

    @Override
    public void injectPlayer(Player player) {
        ServerPlayer ply = ((CraftPlayer) player).getHandle();
        ChannelHandler_V1_18_R2 cdh = new ChannelHandler_V1_18_R2(ply);

        ChannelPipeline pipeline = ply.connection.getConnection().channel.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof Connection) {
                pipeline.addBefore(name, "pande_loot_packet_handler", cdh);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.getConnection().channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("pande_loot_packet_handler");
            return null;
        });
    }

    @Override
    public void updateHologramPosition(double x, double y, double z, Entity hologram, Player player) {
        if (player == null) return;
        ArmorStand stand = ((CraftArmorStand) hologram).getHandle();
        //ArmorStand bArmorStand = (((ArmorStand) stand.getBukkitEntity()));
        int id = stand.getId();
        stand.setPos(x, y, z);
        ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(id, stand.getEntityData(), true);
        Connection connection = ((CraftPlayer) player).getHandle().connection.getConnection();
        connection.send(new ClientboundTeleportEntityPacket(stand));
        connection.send(metadata);
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

}
