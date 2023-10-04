package fr.adrackiin.hydranium.api.exceptions;

import java.util.UUID;

@SuppressWarnings("serial")
public class AccountNotFoundException extends Exception{

    public AccountNotFoundException(UUID uuid) { //Si compte non trouvé

        super("Le compte "+uuid.toString()+" n'a pas été trouvé");

    }

}
