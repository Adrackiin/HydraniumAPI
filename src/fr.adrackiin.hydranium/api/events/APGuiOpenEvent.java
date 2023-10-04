package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.gui.APGuiListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class APGuiOpenEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final APGuiListener gui;

    public APGuiOpenEvent(Player player, APGuiListener gui) {
        this.player = player;
        this.gui = gui;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public APGuiListener getGui() {
        return gui;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
