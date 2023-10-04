package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.exceptions.APGuiChildrenNotFoundException;
import fr.adrackiin.hydranium.api.utils.statics.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class APGui implements Cloneable {

    //private static int ID = 0;

    //private int id;
    private String name;
    private final byte lines;
    private ArrayList<APGuiItem> items = new ArrayList<>();
    private Inventory gui;
    private final APGui parent;
    private final HashMap<String, APGui> childrenName = new HashMap<>();
    private final HashMap<Byte, APGui> childrenSlot = new HashMap<>();
    private final boolean fill;
    private final boolean back;
    private Object source;

    public APGui(String name, byte lines, APGuiItem[] items, APGui parent, boolean fill, boolean back) {
        //this.id = ID;
        //ID ++;
        this.lines = lines;
        this.items.addAll(Arrays.asList(items));
        this.parent = parent;
        this.name = name;
        this.fill = fill;
        this.back = back;
        create();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object clone() throws CloneNotSupportedException{
        APGui tmp = (APGui) super.clone();
        tmp.items = (ArrayList) this.items.clone();
        return tmp;
    }

    @Override
    public String toString() {
        return "APGui{" +
                ", name='" + name + '\'' +
                ", lines=" + lines +
                ", items=" + items.toString() +
                ", gui=" + gui +
                ", parent=" + parent +
                ", childrenName=" + childrenName +
                ", childrenSlot=" + childrenSlot +
                ", fill=" + fill +
                ", back=" + back +
                '}';
    }

    @Override
    protected void finalize() {
        APICore.getPlugin().logServer("APGui removed successfully: " + this.getName() + " !");
    }

    public void create(){
        this.gui = Bukkit.createInventory(null, lines * 9, this.parent == null ? this.name : this.parent.getName() + " §8» " + this.name);
        if(this.fill){
            for(byte i = 0; i < lines * 9; i ++){
                this.gui.setItem(i, APGuiListener.blackBorder.getItem());
            }
        }
        for(APGuiItem item : this.items){
            this.gui.setItem(item.getSlot(), item.getItem());
        }
        if(this.back && parent != null){
            this.gui.setItem((lines - 1) * 9, APGuiListener.back.getItem());
        }
    }

    public void addItem(APGuiItem item){
        items.add(item);
        update(item.getSlot());
    }

    public void removeItem(APGuiItem item){
        removeItem(item.getSlot());
    }

    public void removeItem(byte slot){
        items.removeIf(apGuiItem -> apGuiItem.getSlot() == slot);
    }

    public void update(byte slot){
        if(this.getItem(slot) == null){
            this.gui.setItem(slot, ItemCreator.simpleItem(Material.AIR));
            return;
        }
        this.gui.setItem(slot, getItem(slot).getItem());
    }

    public void open(APPlayer player){
        if(source != null){
            if(source instanceof APGMultiplePlayer){
                APGMultiplePlayer apgmp = (APGMultiplePlayer) source;
                if(!apgmp.getAPGuis().contains(player.getUUID())){
                    try {
                        APGui temp = (APGui) this.clone();
                        apgmp.getAPGuis().put(player.getUUID(), temp);
                        apgmp.create(player);
                        temp.create();
                    } catch(CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                }
                if(source instanceof APGOpenCustom){
                    ((APGOpenCustom) source).open(player);
                    return;
                }
                player.openInventory(apgmp.getAPGui(player).getGui());
                return;
            }
            if(source instanceof APGMultiplePlayerAsync){
                APGMultiplePlayerAsync apgmp = (APGMultiplePlayerAsync) source;
                if(!apgmp.getAPGuis().contains(player.getUUID())){
                    try {
                        APGui temp = (APGui) this.clone();
                        apgmp.getAPGuis().put(player.getUUID(), temp);
                        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
                            apgmp.create(player);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()->{
                                temp.create();
                                if(source instanceof APGOpenCustom){
                                    ((APGOpenCustom) source).open(player);
                                    return;
                                }
                                player.openInventory(apgmp.getAPGui(player).getGui());
                            });
                        });
                    } catch(CloneNotSupportedException e){
                        e.printStackTrace();
                    }
                    return;
                }
                if(source instanceof APGOpenCustom){
                    ((APGOpenCustom) source).open(player);
                    return;
                }
                player.openInventory(apgmp.getAPGui(player).getGui());
                return;
            }
            if(source instanceof APGMultiple){
                try {
                    ((APGMultiple)source).onOpen(player, (APGui)this.clone());
                } catch(CloneNotSupportedException e){
                    e.printStackTrace();
                }
                return;
            }
            if(source instanceof APGUnique){
                APGUniqueOpenEvent e = new APGUniqueOpenEvent(name);
                APICore.getPlugin().getServer().getPluginManager().callEvent(e);
                try {
                    APGui temp = (APGui) this.clone();
                    ((APGUnique)temp.getSource()).create(e.getObject());
                    temp.create();
                } catch(CloneNotSupportedException err){
                    err.printStackTrace();
                }
            }
        }
        player.openInventory(this.gui);
    }

    public void addChildren(APGui gui, byte slot){
        if(childrenName.containsKey(gui.getName()) || childrenSlot.containsKey(slot)){
            return;
        }
        this.childrenName.put(gui.getName(), gui);
        this.childrenSlot.put(slot, gui);
    }

    public APGui getChildren(String name) throws APGuiChildrenNotFoundException{
        if(this.childrenName.get(name) == null)throw new APGuiChildrenNotFoundException(name, this);
        return this.childrenName.get(name);
    }

    public APGui getChildren(byte slot) throws APGuiChildrenNotFoundException {
        if(this.childrenSlot.get(slot) == null)throw new APGuiChildrenNotFoundException();
        return this.childrenSlot.get(slot);
    }

    public APGuiItem getItem(byte slot){
        int i = 0, itemsSize = items.size();
        APGuiItem toReturn = null;
        while(i < itemsSize && toReturn == null){
            APGuiItem item = items.get(i);
            if(item.getSlot() == slot){
                toReturn = item;
            }
            i++;
        }
        return toReturn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
        this.create();
    }

    public byte getLines() {
        return lines;
    }

    public List<APGuiItem> getItems() {
        return items;
    }

    public APGui getParent() {
        return parent;
    }

    public boolean isFill() {
        return fill;
    }

    public boolean isBack() {
        return back;
    }

    public Object getSource() {
        return source;
    }

    public APGui setSource(Object source){
        this.source = source;
        return this;
    }

    public Inventory getGui(){
        return gui;
    }

}
