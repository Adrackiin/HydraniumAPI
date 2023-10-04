package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class EasterEggsCmd implements APCommandListener {

    private final APCommand command;

    public EasterEggsCmd(){
        this.command = new APCommand(
                "eastereggs",
                new String[]{"me", "/calc", "yo", "allo", "skript", "sk"},
                "Trololololol",
                new String[]{},
                "hydranium.api.command.eastereggs"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        switch(command){
            case "eastereggs":
                player.sendMessage("§aCot cot cot");
                break;
            case "me":
                player.sendMessageFromConsole("Ptdr t'es qui ?");
                break;
            case "/calc":
                player.sendMessage("§4Tentative de §ccra§ks§ch §4§kd§4u §c§ks§cerv§ke§4u§cr §4§ke§cn c§4o§ku§cr§ks§c §km§4§kd§c§kr");
                break;
            case "yo":
                player.sendMessage("§aPlait");
                break;
            case "allo":
                player.sendMessage("§aA l'huile");
                break;
            case "skript":
            case "sk":
                player.sendMessage("§aTous les plugins ont été développés en Java par Adrackiin ;) Pas de skript ici !");
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
