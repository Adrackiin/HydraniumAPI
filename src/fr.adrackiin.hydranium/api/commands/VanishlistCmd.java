package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class VanishlistCmd implements APCommandListener {

    private final APCommand command;

    public VanishlistCmd(){
        this.command = new APCommand(
                "vanishlist",
                new String[]{"vlist"},
                "Liste des joueurs invisible",
                new String[]{},
                "hydranium.api.command.vanishlist"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        return "Commande en construction";
        /*StringBuilder stringBuilder = new StringBuilder();
        for(APPlayer p : APICore.getPlugin().getAPPlayers()){
            if(!p.isVisible()){
                stringBuilder.append(p.getName()).append(", ");
            }
        }
        player.sendMessage(Prefix.hydranium + "§aJoueurs vanish: " + stringBuilder.substring(0, stringBuilder.length() - 2));
        return null;*/
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
