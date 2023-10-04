package fr.adrackiin.hydranium.api.gui;

import fr.adrackiin.hydranium.api.utils.statics.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class APGuiItem implements Cloneable {

    private byte slot;
    private boolean glow;
    private final ItemStack item;
    private String permission;

    public APGuiItem(Material type, short data, byte amount, byte slot, String name, String[] description) {
        this.slot = slot;
        this.glow = false;
        this.item = ItemCreator.getItem(type, name, amount, data, (description == null ? null : Arrays.asList(description)));
        ItemMeta metaTemp = this.item.getItemMeta();
        metaTemp.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        this.item.setItemMeta(metaTemp);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "APGuiItem{" +
                "type=" + getType() +
                ", data=" + getData() +
                ", slot=" + slot +
                ", amount=" + getAmount() +
                ", name='" + getName() + '\'' +
                ", description=" + getDescription() +
                ", glow=" + glow +
                ", item=" + item +
                '}';
    }

    public APGuiItem get(byte slot){
        APGuiItem temp;
        try {
            temp = (APGuiItem) this.clone();
            temp.setSlot(slot);
            return temp;
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public APGuiItem get(String name){
        APGuiItem temp;
        try {
            temp = (APGuiItem) this.clone();
            temp.setName(name);
            return temp;
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPermission(){
        return permission;
    }

    public APGuiItem setPermission(String permission){
        this.permission = permission;
        return this;
    }

    public Material getType() {
        return this.item.getType();
    }

    public APGuiItem setType(Material type){
        this.item.setType(type);
        return this;
    }

    public short getData() {
        return this.item.getDurability();
    }

    public APGuiItem setData(short data){
        this.item.setDurability(data);
        return this;
    }

    public byte getSlot() {
        return slot;
    }

    public void setSlot(byte slot) {
        this.slot = slot;
    }

    public byte getAmount() {
        return (byte)this.item.getAmount();
    }

    public String getName() {
        return this.item.getItemMeta().getDisplayName();
    }

    public APGuiItem setName(String name) {
        ItemMeta metaTemp = this.item.getItemMeta();
        metaTemp.setDisplayName(name);
        this.item.setItemMeta(metaTemp);
        return this;
    }

    public List<String> getDescription() {
        return this.getItem().getItemMeta().getLore();
    }

    public APGuiItem setDescription(List<String> description) {
        ItemMeta metaTemp = this.item.getItemMeta();
        metaTemp.setLore(description);
        this.item.setItemMeta(metaTemp);
        return this;
    }

    public boolean isGlow() {
        return glow;
    }

    public APGuiItem setGlow(boolean glow) {
        if(this.glow == glow){
            return this;
        }
        if(glow){
            ItemCreator.addGlowingEffect(this.item);
        } else {
            ItemCreator.removeGlowingEffect(this.item);
        }
        this.glow = glow;
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public APGuiItem setHeadName(String name){
        SkullMeta temp = (SkullMeta)this.item.getItemMeta();
        temp.setOwner(name);
        this.item.setItemMeta(temp);
        return this;
    }
}
