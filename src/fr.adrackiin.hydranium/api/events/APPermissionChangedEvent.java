package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class APPermissionChangedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final String permission;
    private final APPlayer player;
    private final boolean hasPermission;

    public APPermissionChangedEvent(String permission, APPlayer player, boolean hasPermission){
        this.permission = permission;
        this.player = player;
        this.hasPermission = hasPermission;
    }

    public boolean hasPermission(){
        return hasPermission;
    }

    public String getPermission(){
        return permission;
    }

    public APPlayer getPlayer(){
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
