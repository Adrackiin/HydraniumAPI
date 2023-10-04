package fr.adrackiin.hydranium.api.commands.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.HistoryPlayer;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class UnbanCmd implements APCommandListener {

    private final APCommand command;

    public UnbanCmd(){
        this.command = new APCommand(
                "unban",
                new String[]{"deban"},
                "Débannir un joueur",
                new String[]{"<joueur>"},
                "hydranium.api.command.unban"
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
    @SuppressWarnings("unchecked")
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        OfflinePlayer target = Players.getOfflinePlayer(args[0]);
        if(!APICore.getPlugin().getPlayerBanned().contains(target.getUniqueId())){
            return  "Ce joueur n'est pas banni";
        }
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            UUID uuid = target.getUniqueId();
            HistoryPlayer history = new HistoryPlayer();
            try {
                history.deserialize((String)APICore.getPlugin().getDataManager().get(uuid, "history"));
            } catch(CanTakeTimeException ignored){}
            List hist = history.getLastBan();
            if(hist == null){
                player.sendMessage("§c§lErreur: §7Ce joueur n'est pas banni (ou historique introuvable)");
                return;
            }
            hist.set(6, player.getName());
            APICore.getPlugin().getDataManager().set(uuid, "history", history.serialize());
            player.sendMessage(Prefix.odin + "§7Vous avez débanni §c" + target.getName());
            PubSub.unbanPlayer(uuid);
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }

}
