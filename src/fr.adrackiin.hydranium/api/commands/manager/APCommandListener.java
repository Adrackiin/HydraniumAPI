package fr.adrackiin.hydranium.api.commands.manager;

import fr.adrackiin.hydranium.api.core.APPlayer;

public interface APCommandListener {

    /**
     * Get syntax id of command's argument
     * @param args Arguments
     * @return Id of Syntax, -1 otherwise
     */
    int getSyntax(String[] args);
    String onCommand(String command, APPlayer player, String[] args, short syntax);
    APCommand getCommand();




}
