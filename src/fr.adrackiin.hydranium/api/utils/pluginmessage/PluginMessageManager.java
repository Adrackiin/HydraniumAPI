package fr.adrackiin.hydranium.api.utils.pluginmessage;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.management.data.Data;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class PluginMessageManager {

    private static final String BUNGEECORD_CHANNEL = "BungeeCord";
    private static final APICore apiCore = APICore.getPlugin();

    public static void registerChannels(){
        apiCore.getServer().getMessenger().registerIncomingPluginChannel(apiCore, BUNGEECORD_CHANNEL, new PluginMessageReceived());
        apiCore.getServer().getMessenger().registerOutgoingPluginChannel(apiCore, BUNGEECORD_CHANNEL);
    }

    public static void connect(UUID uuid){
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        APICore.getPlugin().getDataManager().set(uuid, new Data("stats", player.getStats().serialize()), new Data("settings", player.getSettings().serialize()));
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF("lobby");

        player.getPlayer().sendPluginMessage(apiCore, BUNGEECORD_CHANNEL, out.toByteArray());
    }

    public static void playerCount(Player player, String server){
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        player.sendPluginMessage(apiCore, BUNGEECORD_CHANNEL, out.toByteArray());
    }

    public static void kickBanPlayer(String pseudo, String raison){
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("KickPlayer");
        out.writeUTF(pseudo);
        out.writeUTF(Prefix.odin + "§c" + "Vous avez été banni pour " + "§6" + raison);

        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(apiCore, BUNGEECORD_CHANNEL, out.toByteArray());
    }

    public static void kickPlayer(String pseudo, String reason){
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("KickPlayer");
        out.writeUTF(pseudo);
        if(reason.equalsIgnoreCase("")){
            out.writeUTF(Prefix.odin + "§c" + "Vous avez été kick");
        } else {
            out.writeUTF(Prefix.odin + "§c" + "Vous avez été kick pour " + "§6" + reason);
        }

        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(apiCore, BUNGEECORD_CHANNEL, out.toByteArray());
    }

    public static void receivePluginMessage(String channel, Player player, byte[] bytes) throws IOException{
        if(!channel.equals(PluginMessageManager.BUNGEECORD_CHANNEL)){
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        final String subChannel = in.readUTF();

        switch(subChannel){
            case "Connect":
                break;
            case "PlayerKick":
                break;
            case "PlayerCount":
                String server = in.readUTF();
                int playercount = in.readInt();
                if(server.equals("ALL")){
                    player.sendMessage(Prefix.hydranium + "§b" + "Il y a " + "§6" + playercount + "§b" + " joueurs sur le serveur");
                } else {
                    player.sendMessage(Prefix.hydranium + "§7" + "Il y a " + "§b" + playercount + "§7" + " joueurs sur le serveur " + "§b" + server);
                }
            default:
                apiCore.logServer("Sub channel inexistant: " + subChannel);
                break;

        }
    }
}
