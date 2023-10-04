package fr.adrackiin.hydranium.api.commands.manager;

import fr.adrackiin.hydranium.api.core.APPlayer;

public interface APServerCommandListener {

    String onCommand(String command, APPlayer player, String[] args, short syntax);

    APCommand getCommand();

}
