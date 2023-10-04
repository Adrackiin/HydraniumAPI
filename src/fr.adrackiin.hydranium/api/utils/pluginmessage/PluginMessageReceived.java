package fr.adrackiin.hydranium.api.utils.pluginmessage;

import fr.adrackiin.hydranium.api.APICore;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.IOException;

public class PluginMessageReceived implements PluginMessageListener {

    private static final APICore apiCore = APICore.getPlugin();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        try {
            PluginMessageManager.receivePluginMessage(channel, player, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
