package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.utils.statics.StringUtils;
import org.bukkit.Material;

public class APGuiItemConfigure extends APGuiItem {

    public APGuiItemConfigure(short data, String name, byte slot) {
        super(Material.STAINED_CLAY, data, (byte)1, slot, name, null);
    }

    public APGuiItem get(float modifier, byte line) {
        APGuiItemConfigure temp = this.manualClone();
        temp.setName(getName() + (modifier > 0 ? "+" : "") + StringUtils.toIntegerString(modifier));
        temp.setSlot((byte)((line - 1) * 9 + this.getSlot()));
        return temp;
    }

    private APGuiItemConfigure manualClone() {
        return new APGuiItemConfigure(this.getData(), this.getName(), this.getSlot());
    }

}
