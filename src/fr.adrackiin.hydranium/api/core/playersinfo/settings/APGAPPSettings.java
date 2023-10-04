package fr.adrackiin.hydranium.api.core.playersinfo.settings;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.gui.APGMultiplePlayer;
import fr.adrackiin.hydranium.api.gui.APGui;
import fr.adrackiin.hydranium.api.gui.APGuiClickEvent;
import fr.adrackiin.hydranium.api.gui.APGuiItem;
import fr.adrackiin.hydranium.api.gui.APGuiListener;
import fr.adrackiin.hydranium.api.utils.APHash;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.UUID;

public class APGAPPSettings extends APGuiListener implements APGMultiplePlayer {

    private final APGui gui;
    private final APHash<UUID, APGui> guis = new APHash<>();

    public APGAPPSettings() {
        this.gui = new APGui("§6Options", (byte)3, new APGuiItem[]{
                new APGuiItem(Material.PAPER, (short)0, (byte)1, (byte)13, "§cMessages privés", null)
        }, null, true, false);
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        if(e.getSlot() == 13){
            e.getPlayer().setPrivateMessage(!e.getPlayer().hasPrivateMessage());
        }
    }

    @Override
    public APGui getAPGui(APPlayer player){
        return guis.get(player.getUUID());
    }

    @Override
    public APHash<UUID, APGui> getAPGuis(){
        return guis;
    }

    @Override
    public void create(APPlayer player){
        boolean privateMessages = (boolean)player.getSettings().get("private-message");
        this.getAPGui(player).getItem((byte)13).setName((privateMessages ? "§a" : "§c") + "Messages privés").setDescription(Arrays.asList("",
                "§7Activer / Désactiver", "§7ses messages privés", "", "§6État: " + (privateMessages ? "§aActivé" : "§cDésactivé")));
    }
}
