package fr.adrackiin.hydranium.api.guis;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.api.utils.enumeration.SanctionReason;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class APGMute extends APGuiListener implements APGUnique {

    private final APGui gui;

    public APGMute() {
        this.gui = new APGui("§cMute", (byte)5, new APGuiItem[]{
                new APGuiItem(Material.BLAZE_POWDER, (short)0, (byte)1, (byte)10, "§c" + SanctionReason.INSULTE.getReason(), null),
                new APGuiItem(Material.BOW, (short)0, (byte)1, (byte)11, "§c" + SanctionReason.SPAM.getReason(), null),
                new APGuiItem(Material.PAPER, (short)0, (byte)1, (byte)12, "§c" + SanctionReason.DOX.getReason(), null),
                new APGuiItem(Material.DIAMOND_CHESTPLATE, (short)0, (byte)1, (byte)13, "§c" + SanctionReason.SPOILSTUFF.getReason(), null)
        }, null, false, false);
        APICore.getPlugin().getAPGuiManager().add(this);
    }

    @Override
    public APGui getAPGui() {
        return gui;
    }

    @Override
    public void onClick(APGuiClickEvent e) {
        ItemStack itemNamePlayer = this.getAPGui().getItem((byte)40).getItem();
        String playerMutedName = ((SkullMeta) itemNamePlayer.getItemMeta()).getOwner();
        OfflinePlayer playerMuted;
        if (Bukkit.getPlayerExact(playerMutedName) == null) {
            playerMuted = Players.getOfflinePlayer(playerMutedName);
        } else {
            playerMuted = Bukkit.getPlayerExact(playerMutedName);
        }
        String reason;
        int time;
        switch (e.getSlot()) {
            case 10:
                reason = SanctionReason.INSULTE.getReason();
                time = SanctionReason.INSULTE.getTime();
                break;
            case 11:
                reason = SanctionReason.SPAM.getReason();
                time = SanctionReason.SPAM.getTime();
                break;
            case 12:
                reason = SanctionReason.DOX.getReason();
                time = SanctionReason.DOX.getTime();
                break;
            case 13:
                reason = SanctionReason.SPOILSTUFF.getReason();
                time = SanctionReason.SPOILSTUFF.getTime();
                break;
            default:
                return;
        }
        e.getPlayer().closeInventory();
        APICore.getPlugin().getPlayerManager().mutePlayer(reason, time, playerMuted, e.getPlayer().getName());
    }

    @Override
    public void create(Object name){
        this.getAPGui().addItem(new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)40, (String)name, null).setHeadName((String)name));
    }
}
