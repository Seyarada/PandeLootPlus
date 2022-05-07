package net.seyarada.pandeloot.utils;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtils {

    public static ItemStack getItemFromSection(ConfigurationSection config) {
        String strMaterial = config.getString("Material");
        String strDisplay = config.getString("Display");
        String strSkull = config.getString("Skull");
        List<String> strLore = config.getStringList("Lore");
        int intModel = config.getInt("Model");
        if(strMaterial==null) return null;

        Material material = Material.valueOf(strMaterial.toUpperCase());
        ItemStack iS = new ItemStack(material);
        ItemMeta meta = iS.getItemMeta();

        if(intModel>0) meta.setCustomModelData(intModel);
        if(strDisplay!=null) meta.setDisplayName(ChatUtils.translateHexCodes(strDisplay));
        if(strLore.size()>0) {
            List<String> lore = new ArrayList<>();
            config.getStringList("Lore").forEach(s -> lore.add(ChatUtils.translateHexCodes(s)));
            meta.setLore(lore);
        }
        iS.setItemMeta(meta);

        if(strSkull!=null) ItemUtils.getCustomTextureHead(iS, strSkull);

        return iS;
    }

    public static ItemStack getCustomTextureHead(ItemStack head, String value) {
        return NMSManager.get().getCustomTextureHead(head, value);
    }

    public static void writeData(ItemStack iS, NamespacedKey key, String data) {
        ItemMeta meta = iS.getItemMeta();
        if(meta==null) return;
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
        iS.setItemMeta(meta);
    }

    public static void removeData(ItemStack iS, NamespacedKey key) {
        ItemMeta meta = iS.getItemMeta();
        if(meta==null) return;
        meta.getPersistentDataContainer().remove(key);
        iS.setItemMeta(meta);
    }

    public static void setFlags(ItemStack iS, FlagPack pack) {
        ItemMeta meta = iS.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(Constants.KEY, PersistentDataType.STRING, pack.toString());
            //meta.getPersistentDataContainer().set(NamespacedKey.fromString("debug"), PersistentDataType.STRING, UUID.randomUUID().toString());
            iS.setItemMeta(meta);
        }
    }

    public static FlagPack getFlags(Item item) {
        ItemMeta meta = item.getItemStack().getItemMeta();
        if(meta==null) {
            return null;
        }
        String str = meta.getPersistentDataContainer().get(Constants.KEY, PersistentDataType.STRING);
        return FlagPack.cache.get(str);
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

}
