package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;

public class BroadcastCmd implements APCommandListener {

    private final APCommand command;

    public BroadcastCmd(){
        this.command = new APCommand(
                "broadcast",
                new String[]{"bc"},
                "Faire une annonce générale",
                new String[]{"<message>"},
                "hydranium.api.command.broadcast"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }
        String broadcast = stringBuilder.substring(0, stringBuilder.length() - 1);
        APICore.getPlugin().getPlayerManager().sendMessageAll(Prefix.broadcast + "§b§l" + broadcast);
        APICore.getPlugin().getPlayerManager().sendMessageBarAll(Prefix.broadcast + "§b§l" + broadcast);
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }

}
