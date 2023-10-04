package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import org.bukkit.Bukkit;

public class PermissionCmd implements APCommandListener {

    private final APCommand command;

    public PermissionCmd(){
        this.command = new APCommand(
                "permission",
                new String[]{},
                "Gérer les permissions",
                new String[]{"has <joueur> <permission> - Affiche si le joueur possède cette permission",
                        "add <joueur> <permission> - Ajoute cette permission au joueur",
                        "remove <joueur> <permission> - Enlève cette permission au joueur"},
                "hydranium.api.command.permission"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 3){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "get":
                return 1;
            case "add":
                return 2;
            case "remove":
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(!APICore.getPlugin().doesAPPlayerExists(args[1]) || Bukkit.getPlayer(args[1]) == null){
            return "Ce joueur n'est pas connecté";
        }
        APPlayer target = APICore.getPlugin().getAPPlayer(args[1]);
        switch(syntax){
            case 1:
                player.sendMessage(Prefix.hydranium + "§c" + target.getName() + (target.hasPermission(args[2]) ? " §apossède" : "§c ne possède pas") + "§7 la permission " + args[2]);
                break;
            case 2:
                if(target.hasPermission(args[2])){
                    return "Ce joueur possède déjà cette permission";
                }
                target.addPermission(args[2]);
                player.sendMessage(Prefix.hydranium + "§a" + target.getName() + "§7 a reçu la permission §a" + args[2]);
                break;
            case 3:
                if(!target.hasPermission(args[2])){
                    return "Ce joueur ne possède pas cette permission";
                }
                target.removePermission(args[2]);
                player.sendMessage(Prefix.hydranium + "§a" + target.getName() + "§7 a perdu la permission §a" + args[2]);
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
