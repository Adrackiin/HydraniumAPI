package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class APGuiCloseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final APPlayer player;
    private final APGuiListener gui;

    public APGuiCloseEvent(APPlayer player, APGuiListener gui) {
        this.player = player;
        this.gui = gui;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public APPlayer getPlayer() {
        return player;
    }

    public APGuiListener getGui() {
        return gui;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
