package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;

public class BroadcastAllCmd implements APCommandListener {

    private final APCommand command;

    public BroadcastAllCmd(){
        this.command = new APCommand(
                "broadcastall",
                new String[]{"bcall"},
                "Faire une annonce sur tous les serveurs",
                new String[]{"<message>"},
                "hydranium.api.command.broadcastall"
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
        for(String arg : args) {
            stringBuilder.append(arg).append(" ");
        }
        PubSub.broadcast(Prefix.broadcast + "§b§l" + stringBuilder.substring(0, stringBuilder.length() - 1));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
