package fr.adrackiin.hydranium.api.gui;

import org.bukkit.Material;

public abstract class APGuiListener {

    protected static final APGuiItem blackBorder = new APGuiItem(Material.STAINED_GLASS_PANE, (byte)7, (byte)1, (byte)0, " ", null);
    protected static final APGuiItem redBorder = new APGuiItem(Material.STAINED_GLASS_PANE,  (byte)14, (byte)1, (byte)0, " ", null);
    protected static final APGuiItemConfigure highLess = new APGuiItemConfigure((byte)14, "§4", (byte)0);
    protected static final APGuiItemConfigure less = new APGuiItemConfigure((byte)1, "§c", (byte)1);
    protected static final APGuiItemConfigure fewLess = new APGuiItemConfigure((byte)4, "§6", (byte)2);
    protected static final APGuiItemConfigure fewMore = new APGuiItemConfigure((byte)3, "§b", (byte)6);
    protected static final APGuiItemConfigure more = new APGuiItemConfigure((byte)5, "§a", (byte)7);
    protected static final APGuiItemConfigure highMore = new APGuiItemConfigure((byte)13, "§2", (byte)8);
    protected static final APGuiItem activate = new APGuiItemConfigure((byte)13, "§2Activer", (byte)6);
    protected static final APGuiItem deactivate = new APGuiItemConfigure((byte)14, "§4Désactiver", (byte)2);
    protected static final APGuiItem validate = new APGuiItem(Material.INK_SACK, (byte)10, (byte)1, (byte)0, "§aValider", null);
    protected static final APGuiItem cancel = new APGuiItem(Material.INK_SACK, (byte)1, (byte)1, (byte)0, "§cAnnuler", null);
    protected static final APGuiItem back = new APGuiItem(Material.STAINED_GLASS_PANE, (byte)14, (byte)1, (byte)0, "§cRetour", null);

    public abstract APGui getAPGui();

    public abstract void onClick(APGuiClickEvent e);

}
