package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.pluginmessage.PluginMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LobbyCmd implements APCommandListener {

    private final APCommand command;

    public LobbyCmd(){
        this.command = new APCommand(
                "lobby",
                new String[]{"hub", "spawn"},
                "Retourner au Lobby",
                new String[]{},
                "hydranium.api.command.lobby"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(APICore.getPlugin().getServerType().equals(APICore.Type.LOBBY)){
            player.teleport(new Location(Bukkit.getWorld("world"), 0.5, 64.5, 0.5));
        } else {
            player.sendMessage(Prefix.hydranium + "§aTéléportation au lobby");
            PluginMessageManager.connect(player.getUUID());
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
