package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;

public class FlyCmd implements APCommandListener {

    private final APCommand command;

    public FlyCmd(){
        this.command = new APCommand(
                "fly",
                new String[]{},
                "Activer / Désactiver son fly",
                new String[]{},
                "hydranium.api.command.fly"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        player.getPlayer().setAllowFlight(!player.getPlayer().getAllowFlight());
        player.sendMessage(Prefix.hydranium + "§6Fly: " + (player.getPlayer().getAllowFlight() ?  "§aActivé" : "§cDésactivé"));
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
