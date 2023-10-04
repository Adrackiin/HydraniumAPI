package fr.adrackiin.hydranium.api.exceptions;

import fr.adrackiin.hydranium.api.APICore;

public class APGuiNotFoundException extends Exception {

    public APGuiNotFoundException() {
        super();
    }

    public APGuiNotFoundException(String name) {
        super("No APGui Found: " + name);
        APICore.getPlugin().logServer(APICore.getPlugin().getAPGuiManager().getGuis().values() + "");
    }
}
