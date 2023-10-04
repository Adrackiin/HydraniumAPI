package fr.adrackiin.hydranium.api.utils.packet;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.utils.APSet;
import io.netty.channel.*;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class APPacket implements Listener {

    private final APSet<APPacketListener> listenersIn = new APSet<>();
    private final APSet<APPacketListener> listenersOut = new APSet<>();

    public APPacket(){
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event){
        removePlayer(event.getPlayer());
    }

    public void addPacketListener(Context context, APPacketListener listener){
        if(context == Context.READ){
            this.listenersIn.add(listener);
        } else if(context == Context.WRITE){
            this.listenersOut.add(listener);
        }
    }

    private void removePlayer(Player player){
        Channel channel = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    private void injectPlayer(Player player){
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                for(APPacketListener listener : listenersIn.copy()){
                    packet = listener.onPacket(player, (Packet)packet);
                }
                if(packet != null){
                    super.channelRead(channelHandlerContext, packet);
                }
            }
            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
                for(APPacketListener listener : listenersOut.copy()){
                    packet = listener.onPacket(player, (Packet)packet);
                }
                if (packet != null) {
                    super.write(ctx, packet, promise);
                }
            }
        };
        ChannelPipeline channelPipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
        channelPipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public enum Context {

        READ,
        WRITE

    }

}
