package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.Bukkit;

public class ReplyCmd implements APCommandListener {

    private final APCommand command;

    public ReplyCmd(){
        this.command = new APCommand(
                "reply",
                new String[]{"r"},
                "Répondre à un message privé",
                new String[]{"<message>"},
                "hydranium.api.command.reply"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length < 1){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(player.getLastPlayerMsg() == null) {
            return "§7Vous n'avez envoyé aucun message privé avant";
        }
        if(Bukkit.getPlayer(player.getLastPlayerMsg()) == null){
            return "§7Ce joueur n'est pas connecté";
        }
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        player.sendPrivateMessage(APICore.getPlugin().getAPPlayer(player.getLastPlayerMsg()), message.substring(0, message.length() - 1));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
