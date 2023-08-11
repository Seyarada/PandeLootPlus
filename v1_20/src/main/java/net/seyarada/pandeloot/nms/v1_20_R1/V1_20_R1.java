package net.seyarada.pandeloot.nms.v1_20_R1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.advancements.*;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.seyarada.pandeloot.APIConstants;
import net.seyarada.pandeloot.nms.NMSMethods;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class V1_20_R1 implements NMSMethods {

    // region Holograms
    private static final double HOLOGRAM_LINE_OFFSET = 0.22;
    private static final double HOLOGRAM_OFFSET = 1.2;

    @Override
    public List<Entity> hologram(Location location, Player player, List<String> text, JavaPlugin plugin) {
        final ServerLevel wS = ((CraftWorld) location.getWorld()).getHandle();
        double lX = location.getX();
        double lY = location.getY() + HOLOGRAM_OFFSET;
        double lZ = location.getZ();

        List<Display.TextDisplay> holograms = new ArrayList<>();

        Collections.reverse(text);

        for (String msg : text) {
            if (msg == null) continue;
            lY += HOLOGRAM_LINE_OFFSET;

            if (msg.isEmpty()) continue;

            final Display.TextDisplay displayEntity = new Display.TextDisplay(EntityType.TEXT_DISPLAY, wS);
            displayEntity.setPos(lX, lY, lZ);
            displayEntity.setCustomName(Component.literal(msg));
            displayEntity.setCustomNameVisible(true);
            displayEntity.setInterpolationDuration(20);
            displayEntity.setInterpolationDelay(0);

            holograms.add(displayEntity);

            ClientboundAddEntityPacket packetPlayOutSpawnEntity = new ClientboundAddEntityPacket(displayEntity, 1);
            ClientboundSetEntityDataPacket metadata = new ClientboundSetEntityDataPacket(displayEntity.getId(), displayEntity.getEntityData().packDirty());
            sendPacket(player, packetPlayOutSpawnEntity, metadata);
        }

        return holograms.stream().map(s -> (Entity) s.getBukkitEntity()).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void updateHologramPosition(double x, double y, double z, Entity hologram, Player player) {
        if (player == null) return;
        Display.TextDisplay textDisplay = ((CraftTextDisplay) hologram).getHandle();
        textDisplay.setPos(x, y, z);

        sendPacket(player, new ClientboundTeleportEntityPacket(textDisplay));
    }
    // endregion

    // region Toast Display
    static final ResourceLocation TOAST_KEY = new ResourceLocation("pandeloot", "notification");
    static final Map<String, Criterion> CRITERIA = new HashMap<>() {{ put("for_free", new Criterion()); }};
    static final Map<ResourceLocation, AdvancementProgress> PROGRESS_MAP = new HashMap<>() {{
        put(TOAST_KEY, new AdvancementProgress() {{
            update(CRITERIA, new String[][] {{"for_free"}});
            getCriterion("for_free").grant();
        }});
    }};

    @Override
    public void displayToast(Player player, String title, String frame, ItemStack icon) {
        MutableComponent chatTitle = Component.translatable(title);
        FrameType advancementFrame = FrameType.valueOf(frame.toUpperCase());
        net.minecraft.world.item.ItemStack craftIcon = CraftItemStack.asNMSCopy(icon);

        DisplayInfo display = new DisplayInfo(craftIcon, chatTitle, null, null, advancementFrame, true, true, true);

        Advancement advancement = new Advancement(TOAST_KEY, null, display, AdvancementRewards.EMPTY, CRITERIA, new String[][]{}, false);
        
        sendPacket(player, new ClientboundUpdateAdvancementsPacket(
                false, List.of(advancement), new HashSet<>(), PROGRESS_MAP
        ), new ClientboundUpdateAdvancementsPacket(
                false, new ArrayList<>(), new HashSet<>() {{ add(TOAST_KEY); }}, new HashMap<>()
        ));
    }
    // endregion

    // region Connection
    @Override
    public void injectPlayer(Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ChannelHandler_V1_20_R1 channelHandler = new ChannelHandler_V1_20_R1(serverPlayer);

        final ChannelPipeline pipeline = getConnectionWithReflection(player).channel.pipeline();

        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof Connection) {
                pipeline.addBefore(name, APIConstants.CHANNEL_ID, channelHandler);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        final Channel channel = getConnectionWithReflection(player).channel;

        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(APIConstants.CHANNEL_ID);
            return null;
        });
    }

    Connection getConnectionWithReflection(Player player) {
        try {
            Field connectionField = ((CraftPlayer) player).getHandle().connection.getClass().getDeclaredField("h");
            connectionField.setAccessible(true);
            return (Connection) connectionField.get(((CraftPlayer) player).getHandle().connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPacket(Player player, Object... packets) {
        ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
        for (Object obj : packets) {
            if(obj instanceof Packet<?> packet) {
                connection.send(packet);
            }
        }
    }
    // endregion

    @Override
    public void destroyEntity(int entityId, Player player) {
        sendPacket(player, new ClientboundRemoveEntitiesPacket(entityId));
    }

}
