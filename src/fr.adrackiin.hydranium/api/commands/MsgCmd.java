package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.Bukkit;

public class MsgCmd implements APCommandListener {

    private final APCommand command;

    public MsgCmd(){
        this.command = new APCommand(
                "msg",
                new String[]{"mp", "m", "whisper", "w", "tell"},
                "Envoyer un message privé",
                new String[]{"<joueur> <message>"},
                "hydranium.api.command.msg"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length < 2){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(!APICore.getPlugin().doesAPPlayerExists(args[0]) || Bukkit.getPlayer(args[0]) == null){
            return "Ce joueur n'est pas connecté";
        }
        APPlayer target = APICore.getPlugin().getAPPlayer(args[0]);
        if (target == player) {
            player.sendMessage("§7§oVous entendez des voix dans votre tête...");
            return null;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        player.sendPrivateMessage(target, message.substring(0, message.length() - 1));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
