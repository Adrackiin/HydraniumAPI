package fr.adrackiin.hydranium.api.gui;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class APGUniqueOpenEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Object object;
    private String name;

    public APGUniqueOpenEvent(String name){
        this.name = name;
    }

    public APGUniqueOpenEvent(Object object){
        this.object = object;
    }

    public String getName(){
        return name;
    }

    public Object getObject(){
        return object;
    }

    public void setObject(Object object){
        this.object = object;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

}
