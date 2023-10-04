package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APHash;

import java.util.UUID;

/**
 * Used when need to have a gui for each player
 */
public interface APGMultiplePlayer {

    APGui getAPGui(APPlayer player);
    APHash<UUID, APGui> getAPGuis();
    void create(APPlayer player);

}
