package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.core.APPlayer;

public class APGuiClickEvent {

    private final String itemName;
    private final byte slot;
    private final APPlayer player;
    private final boolean clickConfigure;

    public APGuiClickEvent(String itemName, byte slot, APPlayer player, boolean clickConfigure) {
        this.itemName = itemName;
        this.slot = slot;
        this.player = player;
        this.clickConfigure = clickConfigure;
    }

    public String getItemName(){
        return itemName;
    }

    public byte getSlot() {
        return slot;
    }

    public APPlayer getPlayer() {
        return player;
    }

    public boolean isClickConfigure() {
        return clickConfigure;
    }
}
