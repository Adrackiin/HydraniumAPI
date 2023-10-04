package fr.adrackiin.hydranium.api.events;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.accounts.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AccountLoadedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final APPlayer player;
    private final Account account;

    public AccountLoadedEvent(APPlayer player, Account account){
        this.player = player;
        this.account = account;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public APPlayer getPlayer() {
        return player;
    }

    public Account getAccount() {
        return account;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
