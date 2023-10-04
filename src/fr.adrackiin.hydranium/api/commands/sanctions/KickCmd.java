package fr.adrackiin.hydranium.api.commands.sanctions;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.statics.Players;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class KickCmd implements APCommandListener {

    private final APCommand command;

    public KickCmd(){
        this.command = new APCommand(
                "kick",
                new String[]{},
                "Expulser un joueur du serveur",
                new String[]{"<joueur>", "<joueur> <raison>"},
                "hydranium.api.command.kick"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return -1;
        }
        if(args.length == 1){
            return 1;
        }
        return 2;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        OfflinePlayer cible = Players.getOfflinePlayer(args[0]);
        UUID uuid = cible.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            /*AccountManager accountProvider = APICore.getPlugin().getAccountManager();
            Account account = null;
            try {
                account = accountProvider.getAccountRedis(uuid);
            } catch(CanTakeTimeException ignored) { }
            if(account == null){
                player.sendMessage("§c§lErreur: §7Ce joueur n'est pas connecté");
                return;
            }
            if(!account.isConnected()){
                player.sendMessage("§c§lErreur: §7Ce joueur n'est pas connecté");
                return;
            }
            switch(syntax){
                case 1:
                    PluginMessageManager.kickPlayer(cible.getName(), "");
                    break;
                case 2:
                    StringBuilder reason = new StringBuilder();
                    for(short i = 1; i < args.length; i++){ //Raison
                        reason.append(args[i]).append(" ");
                    }
                    PluginMessageManager.kickPlayer(cible.getName(), reason.toString());
                    break;
            }*/
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
