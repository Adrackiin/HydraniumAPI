package fr.adrackiin.hydranium.api.commands.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.HistoryPlayer;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class HistoryCmd implements APCommandListener {

    private final APCommand command;

    public HistoryCmd(){
        this.command = new APCommand(
                "history",
                new String[]{},
                "Casier d'un joueur",
                new String[]{"<joueur> - Historique des bans/mute"},
                "hydranium.api.command.history"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 1){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), () -> {
            HistoryPlayer history = new HistoryPlayer();
            OfflinePlayer target = Players.getOfflinePlayer(args[0]);
            UUID uuid = target.getUniqueId();
            try {
                if(!APICore.getPlugin().getAccountManager().isAccountOnDB(uuid)){
                    player.sendMessage("§c§lErreur: §7Historique non trouvé");
                    return;
                }
            } catch(CanTakeTimeException ignored){}
            try {
                history.deserialize((String) APICore.getPlugin().getDataManager().get(uuid, "history"));
            } catch(CanTakeTimeException ignored){}
            Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
                    if((int)history.get("n") == 0){
                        player.sendMessage("§8=== §aHistorique de §3" + target.getName() + "§8 ===\n\n§aCe joueur n'a jamais subi de sanctions");
                        return;
                    }
                    player.sendMessage("§8========== §aHistorique de §b" + target.getName() + " §8===========");
                    boolean hasExpired;
                    int date;
                    int timeLeft;
                    List historyDetail;
                    for(int i = (int)history.get("n"); i > 0; i --){
                        player.sendMessage("");
                        historyDetail = history.getHistory(i);
                        date = (int)(System.currentTimeMillis() / 1000 - (int)historyDetail.get(3));
                        hasExpired = !historyDetail.get(6).equals("") || ((int)historyDetail.get(5) != -1 && ((int)historyDetail.get(3) + (int)historyDetail.get(5) * 3600) <= (System.currentTimeMillis() / 1000));
                        player.sendMessage("§0=== §c[ §fIl y a §e" + (date / (3600 * 24)) + " §fjours, §e" + ((date % (3600 * 24)) / 3600) + " §fheures et §e" + ((date % (3600 * 24) % 3600) / 60) + "§f minutes §c] " + (hasExpired ? " §8[§aExpiré§8] §0===" : " §8[§cActif§8] §0==="));
                        player.sendMessage("§6" + historyDetail.get(1) + "§7 a été §c" + historyDetail.get(0) + " §7par §6" + historyDetail.get(2) + " §7pour §b" + historyDetail.get(4));
                        if(!hasExpired){
                            if((int)historyDetail.get(5) == -1){
                                player.sendMessage("§7Expiration: §cJamais");
                            } else {
                                timeLeft = ((int)historyDetail.get(5) * 3600  + (int)historyDetail.get(3)) - (int)(System.currentTimeMillis() / 1000);
                                player.sendMessage("§7Expiration: §c" + (timeLeft / (3600 * 24)) + " §7jours, §c" + ((timeLeft % (3600 * 24)) / 3600) + " §7heures, §c" + ((timeLeft % (3600 * 24) % 3600) / 60) + "§7 minutes et §c" + ((timeLeft % (3600 * 24) % 3600) % 60) + " §7secondes");
                            }
                        }
                        if(!historyDetail.get(6).equals("")){
                            player.sendMessage("§a" + historyDetail.get(1) + " §7a été §adé" + historyDetail.get(0) + "§7 par §a" + historyDetail.get(6));
                        }
                    }
            });
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }

}
