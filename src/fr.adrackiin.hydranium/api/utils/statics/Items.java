package fr.adrackiin.hydranium.api.utils.statics;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Items {

    public ItemStack getItem(Material material, String name, int amount, int dataValue, String player) {
        ItemStack item = new ItemStack(material, amount, (byte)dataValue);
        if(material == Material.SKULL_ITEM) {
            SkullMeta metaSkull = (SkullMeta)item.getItemMeta();
            metaSkull.setDisplayName(name);
            metaSkull.setOwner(player);
            item.setItemMeta(metaSkull);
        }
        else {
            ItemMeta metaItem = item.getItemMeta();
            metaItem.setDisplayName(name);
            item.setItemMeta(metaItem);
        }
        return item;
    }

}
