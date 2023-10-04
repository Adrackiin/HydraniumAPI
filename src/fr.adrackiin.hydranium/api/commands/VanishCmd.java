package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class VanishCmd implements APCommandListener {

    private final APCommand command;

    public VanishCmd(){
        this.command = new APCommand(
                "vanish",
                new String[]{"v"},
                "Se mettre en invisible",
                new String[]{},
                "hydranium.api.command.vanish"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        return "Commande en maintenance";
        /*if(player.isVisible()){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.hidePlayer(player.getPlayer());
            }
            player.sendMessage(Prefix.hydranium + "§aVous êtes maintenant invisible");
        } else {
            for(Player p : Bukkit.getOnlinePlayers()){
                p.showPlayer(player.getPlayer());
            }
            player.sendMessage(Prefix.hydranium + "§cVous êtes maintenant visible");
        }
        PubSub.vanish(player);
        return null;*/
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
