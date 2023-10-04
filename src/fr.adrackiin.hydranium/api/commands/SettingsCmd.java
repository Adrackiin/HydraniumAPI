package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;

public class SettingsCmd implements APCommandListener {

    private final APCommand command;

    public SettingsCmd(){
        this.command = new APCommand(
                "options",
                new String[]{"settings", "option", "setting", "set"},
                "Modifier vos options générales",
                new String[]{},
                "hydranium.api.command.options"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        try {
            APICore.getPlugin().getAPGuiManager().get("§6Options").getAPGui().open(player);
        } catch(APGuiNotFoundException e){
            e.printStackTrace();
            return "§7Une erreur interne est survenue";
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
