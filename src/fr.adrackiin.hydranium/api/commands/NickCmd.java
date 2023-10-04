package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NickCmd implements APCommandListener {

    private final APCommand command;

    public NickCmd(){
        this.command = new APCommand(
                "nickname",
                new String[]{"nick", "skin"},
                "Changer de pseudo",
                new String[]{"<pseudo>"},
                "hydranium.api.command.nickname"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length != 1){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(command.equals("skin")){

        } else {
            if(args[0].length() > 16){
                return "§7Pseudo trop long";
            }
            player.setNickname(args[0]);
            for(Player pl : Bukkit.getOnlinePlayers()){
                pl.hidePlayer(player.getPlayer());
                pl.showPlayer(player.getPlayer());
            }
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
