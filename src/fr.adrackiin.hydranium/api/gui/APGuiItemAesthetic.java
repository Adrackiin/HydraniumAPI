package fr.adrackiin.hydranium.api.gui;

import org.bukkit.Material;

public class APGuiItemAesthetic extends APGuiItem {
    public APGuiItemAesthetic(Color color, byte slot) {
        super(Material.STAINED_GLASS_PANE, color.getData(), (byte)1, slot, " ", null);
    }

    public enum Color {

        WHITE((byte)0),
        ORANGE((byte)1),
        MAGENTA((byte)2),
        LIGHT_BLUE((byte)3),
        YELLOW((byte)4),
        GREEN((byte)5),
        LIGHT_PURPLE((byte)6),
        DARK_GRAY((byte)7),
        GRAY((byte)8),
        DARK_AQUA((byte)9),
        PURPLE((byte)10),
        BLUE((byte)11),
        BROWN((byte)12),
        DARK_GREEN((byte)13),
        RED((byte)14),
        BLACK((byte)15);

        private final byte data;

        Color(byte data) {
            this.data = data;
        }

        public byte getData() {
            return data;
        }
    }

}
