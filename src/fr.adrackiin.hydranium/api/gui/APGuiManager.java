package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.playersinfo.settings.APGAPPSettings;
import fr.adrackiin.hydranium.api.events.APGuiCloseEvent;
import fr.adrackiin.hydranium.api.events.APGuiInteractEvent;
import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.api.exceptions.APGuiNotFoundException;
import fr.adrackiin.hydranium.api.guis.APGBan;
import fr.adrackiin.hydranium.api.guis.APGMute;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Hashtable;

public class APGuiManager implements Listener {

    private final Hashtable<String, APGuiListener> guis = new Hashtable<>();

    public APGuiManager() {
        APICore.getPlugin().getServer().getPluginManager().registerEvents(this, APICore.getPlugin());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPostInit(PostInitEvent e){
        add(new APGAPPSettings());
        add(new APGBan());
        add(new APGMute());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClickInventory(APGuiInteractEvent e){
        if(e.getClick() != ClickType.LEFT && e.getClick() != ClickType.RIGHT){
            return;
        }
        if(e.getItem().getPermission() != null && !e.getPlayer().hasPermission(e.getItem().getPermission())){
            return;
        }
        e.getGui().onClick(new APGuiClickEvent(e.getItem().getItem().getItemMeta().getDisplayName(), e.getSlot(), e.getPlayer(), e.getClick() == ClickType.RIGHT));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClose(InventoryCloseEvent e){
        if(!e.getInventory().getName().contains("»")){
            return;
        }
        if(!exists(getPAGuiName(e.getInventory().getName()))){
            return;
        }
        try {
            APGuiListener gui = get(getPAGuiName(e.getInventory().getName()));
            if(gui.getAPGui().getParent() != null){
                Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> {
                    if(e.getPlayer().getOpenInventory().getTitle().equals("container.crafting")) {
                        APICore.getPlugin().getServer().getPluginManager().callEvent(new APGuiCloseEvent(APICore.getPlugin().getAPPlayer(e.getPlayer().getUniqueId()), gui));
                    }
                }, 1L);
            }
        } catch(APGuiNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClose(APGuiCloseEvent e){
        e.getGui().getAPGui().getParent().open(e.getPlayer());
        if(e.getGui().getAPGui().getSource() != null && e.getGui().getAPGui().getSource() instanceof APGCloseCustom){
            ((APGCloseCustom) e.getGui().getAPGui().getSource()).customClosing(e.getPlayer());
        }
    }

    public void closeAll(APPlayer player){
        APGui buffer;
        try {
            buffer = get(" ").getAPGui();
        } catch(APGuiNotFoundException e) {
            e.printStackTrace();
            return;
        }
        buffer.open(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), () -> {
            if(player.getOpenInventory() != null) {
                player.closeInventory();
            }
        }, 2L);
    }

    public void add(Object gui){
        APGuiListener apgui = (APGuiListener)gui;
        this.guis.put(apgui.getAPGui().getName(), apgui);
        apgui.getAPGui().setSource(gui);
    }

    public void remove(String name){
        this.guis.remove(name);
    }

    public APGuiListener get(String name) throws APGuiNotFoundException {
        if(this.guis.get(name) == null)throw new APGuiNotFoundException(name);
        return this.guis.get(name);
    }

    public void removeSpecial(String name){
        this.guis.remove(name);
    }

    public boolean exists(String name){
        return this.guis.containsKey(name);
    }

    public String getPAGuiName(String name){
        return name.split("» ")[name.split("» ").length-1];
    }

    public Hashtable<String, APGuiListener> getGuis() {
        return guis;
    }

}
