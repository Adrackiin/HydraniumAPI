package fr.adrackiin.hydranium.api.utils.statics;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Players {

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String pseudo){
        return Bukkit.getOfflinePlayer(pseudo);
    }

}
