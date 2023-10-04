package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;

public class TestCmd implements APCommandListener {

    private final APCommand command;

    public TestCmd(){
        this.command = new APCommand(
                "test",
                new String[]{},
                "Commande de test",
                new String[]{},
                "hydranium.api.command.test"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] strings){
        return 0;
    }

    @Override
    public String onCommand(String s, APPlayer apPlayer, String[] strings, short i){
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
