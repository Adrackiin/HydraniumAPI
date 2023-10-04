package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class APPlayerJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final APPlayer player;

    public APPlayerJoinEvent(APPlayer player) {
        this.player = player;
    }

    public APPlayer getPlayer() {
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
