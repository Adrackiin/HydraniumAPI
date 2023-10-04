package fr.adrackiin.hydranium.api.channel;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APText;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class Channel implements Listener {
    
    private final String name;
    private final String displayName;
    private final HashSet<UUID> members = new HashSet<>();
    
    public Channel(String name, String displayName){
        this.name = name;
        this.displayName = displayName;
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }
    
    public void addPlayer(UUID uuid){
        if(Bukkit.getPlayer(uuid) != null){
            this.members.add(uuid);
        }
    }
    
    public void sendMessage(APText message, UUID... excludeUUID){
        if(this.isEmpty()){
            return;
        }
        APPlayer player;
        APText msg = new APText(getDisplayName());
        msg.add(message);
        List<UUID> exclude = new ArrayList<>(Arrays.asList(excludeUUID));
        for(UUID uuid : this.members){
            if(Bukkit.getPlayer(uuid) == null){
                removePlayer(uuid);
            } else {
                if(!exclude.contains(uuid)){
                    player = APICore.getPlugin().getAPPlayer(uuid);
                    if(player != null){
                        player.sendMessage(msg);
                    }
                }
            }
        }
    }
    
    public void removePlayer(UUID uuid){
        this.members.remove(uuid);
    }
    
    public boolean isMember(UUID uuid){
        return this.members.contains(uuid);
    }
    
    public void sendMessage(String message, UUID... excludeUUID){
        sendMessage(new APText(message), excludeUUID);
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e){
        removePlayer(e.getPlayer().getUniqueId());
    }
    
    public boolean isEmpty(){
        return this.members.isEmpty();
    }
    
    public String getName(){
        return name;
    }
    
    public String getDisplayName(){
        return displayName;
    }
    
    public void setPlayer(UUID uuid){
        if(this.isMember(uuid)){
            this.removePlayer(uuid);
        } else {
            this.addPlayer(uuid);
        }
    }
    
}
