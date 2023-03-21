package net.seyarada.pandeloot.nms.v1_19_R3;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.seyarada.pandeloot.nms.NMSManager;

public class ChannelHandler_V1_19_R3 extends ChannelDuplexHandler {

    private final ServerPlayer player;

    public ChannelHandler_V1_19_R3(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

        // Item Visibility
        if (packet instanceof ClientboundAddEntityPacket) {
            int id = ((ClientboundAddEntityPacket) packet).getId();
            if(NMSManager.isHiddenFor(id, player.getBukkitEntity().getUniqueId())) {
                return;
            }
        }

        super.write(ctx, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        super.channelRead(ctx, packet);
    }

}
