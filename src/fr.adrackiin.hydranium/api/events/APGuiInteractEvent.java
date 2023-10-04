package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

public class APGuiInteractEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final APGuiListener gui;
    private final APPlayer player;
    private final ClickType click;
    private final byte slot;
    private final APGuiItem item;

    public APGuiInteractEvent(APGuiListener gui, APPlayer player, ClickType click, byte slot, APGuiItem item) {
        this.gui = gui;
        this.player = player;
        this.click = click;
        this.slot = slot;
        this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public APGuiListener getGui() {
        return gui;
    }

    public APPlayer getPlayer() {
        return player;
    }

    public ClickType getClick() {
        return click;
    }

    public byte getSlot() {
        return slot;
    }

    public APGuiItem getItem() {
        return item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
