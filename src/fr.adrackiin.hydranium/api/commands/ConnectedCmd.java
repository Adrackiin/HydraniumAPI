package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.pluginmessage.PluginMessageManager;

public class ConnectedCmd implements APCommandListener {

    private final APCommand command;

    public ConnectedCmd(){
        this.command = new APCommand(
                "connected",
                new String[]{"co"},
                "Obtenir le nombre de joueurs connectés",
                new String[]{"", "[lobby, uhc]"},
                "hydranium.api.command.connected"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return 1;
        }
        if(args.length > 1){
            return -1;
        }
        switch(args[0].toLowerCase()){
            case "lobby":
            case "uhc":
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        switch(syntax){
            case 1:
                PluginMessageManager.playerCount(player.getPlayer(), "ALL");
                break;
            case 2:
                PluginMessageManager.playerCount(player.getPlayer(), args[0].toLowerCase());
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}