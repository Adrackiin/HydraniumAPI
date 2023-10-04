package fr.adrackiin.hydranium.api.guis;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.gui.*;
import fr.adrackiin.hydranium.api.utils.enumeration.SanctionReason;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class APGBan extends APGuiListener implements APGUnique {

    private final APGui gui;

    public APGBan() {
        this.gui = new APGui("§cBanissement", (byte)5, new APGuiItem[]{
                new APGuiItem(Material.GOLDEN_APPLE, (short)1, (byte)1, (byte)10, "§c" + SanctionReason.CHEAT.getReason(), null),
                new APGuiItem(Material.LEASH, (short)0, (byte)1, (byte)11, "§c" + SanctionReason.STALK.getReason(), null),
                new APGuiItem(Material.LAVA_BUCKET, (short)0, (byte)1, (byte)12, "§c" + SanctionReason.IPVP.getReason(), null),
                new APGuiItem(Material.BED, (short)0, (byte)1, (byte)13, "§c" + SanctionReason.DECO.getReason(), null),
                new APGuiItem(Material.DEAD_BUSH, (short)0, (byte)1, (byte)14, "§c" + SanctionReason.SUICIDE.getReason(), null),
                new APGuiItem(Material.CACTUS, (short)0, (byte)1, (byte)15, "§c" + SanctionReason.SUICIDE_PVP.getReason(), null),
                new APGuiItem(Material.LADDER, (short)0, (byte)1, (byte)16, "§c" + SanctionReason.TOWER.getReason(), null),
                new APGuiItem(Material.TRAP_DOOR, (short)0, (byte)1, (byte)19, "§c" + SanctionReason.TRAP.getReason(), null),
                new APGuiItem(Material.FISHING_ROD, (short)0, (byte)1, (byte)20, "§c" + SanctionReason.CROSSTEAM.getReason(), null),
                new APGuiItem(Material.DIAMOND_PICKAXE, (short)0, (byte)1, (byte)21, "§c" + SanctionReason.MINING.getReason(), null),
                new APGuiItem(Material.DIAMOND_SPADE, (short)0, (byte)1, (byte)22, "§c" + SanctionReason.DIGDOWN.getReason(), null),
                new APGuiItem(Material.BEDROCK, (short)0, (byte)1, (byte)23, "§c" + SanctionReason.BUG.getReason(), null)
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
        String playerBannedName = itemNamePlayer.getItemMeta().getDisplayName().substring(2);
        OfflinePlayer playerBanned;
        if(Bukkit.getPlayerExact(playerBannedName) == null) {
            playerBanned = Players.getOfflinePlayer(playerBannedName);
        } else {
            playerBanned = Bukkit.getPlayerExact(playerBannedName);
        }
        String reason;
        int time;
        switch (e.getSlot()) {
            case 10:
                reason = SanctionReason.CHEAT.getReason();
                time = SanctionReason.CHEAT.getTime();
                break;
            case 11:
                reason = SanctionReason.STALK.getReason();
                time = SanctionReason.STALK.getTime();
                break;
            case 12:
                reason = SanctionReason.IPVP.getReason();
                time = SanctionReason.IPVP.getTime();
                break;
            case 13:
                reason = SanctionReason.DECO.getReason();
                time = SanctionReason.DECO.getTime();
                break;
            case 14:
                reason = SanctionReason.SUICIDE.getReason();
                time = SanctionReason.SUICIDE.getTime();
                break;
            case 15:
                reason = SanctionReason.SUICIDE_PVP.getReason();
                time = SanctionReason.SUICIDE_PVP.getTime();
                break;
            case 16:
                reason = SanctionReason.TOWER.getReason();
                time = SanctionReason.TOWER.getTime();
                break;
            case 19:
                reason = SanctionReason.TRAP.getReason();
                time = SanctionReason.TRAP.getTime();
                break;
            case 20:
                reason = SanctionReason.CROSSTEAM.getReason();
                time = SanctionReason.CROSSTEAM.getTime();
                break;
            case 21:
                reason = SanctionReason. MINING.getReason();
                time = SanctionReason.MINING.getTime();
                break;
            case 22:
                reason = SanctionReason.DIGDOWN.getReason();
                time = SanctionReason.DIGDOWN.getTime();
                break;
            case 23:
                reason = SanctionReason.BUG.getReason();
                time = SanctionReason.BUG.getTime();
                break;
            default:
                return;
        }
        APICore.getPlugin().getPlayerManager().banPlayer(reason, time, playerBanned, e.getPlayer().getName());
    }

    @Override
    public void create(Object name){
        this.getAPGui().addItem(new APGuiItem(Material.SKULL_ITEM, (short)3, (byte)1, (byte)40, (String)name, null).setHeadName((String)name));
    }
}
