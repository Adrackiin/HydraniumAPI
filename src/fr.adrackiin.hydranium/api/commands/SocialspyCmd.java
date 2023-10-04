package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class SocialspyCmd implements APCommandListener {

    private final APCommand command;

    public SocialspyCmd(){
        this.command = new APCommand(
                "socialspy",
                new String[]{"ss"},
                "Voir les messages privés",
                new String[]{},
                "hydranium.api.command.socialspy"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
        APICore.getPlugin().getChannelManager().newChannel("channel.socialspy", "§8[§dSpy§8]§r ");
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        player.setSocialSpy(!player.isSocialSpyer());
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
