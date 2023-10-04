package fr.adrackiin.hydranium.api.exceptions;

import fr.adrackiin.hydranium.api.gui.APGui;

public class APGuiChildrenNotFoundException extends Exception {

    public APGuiChildrenNotFoundException() {
        super();
    }

    public APGuiChildrenNotFoundException(String name, APGui parent) {
        super("No APGuiChildren Found: " + name + " of " +  parent);
    }

}
