package net.seyarada.pandeloot.loot.providers.item;

import net.seyarada.pandeloot.api.ItemProvider;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VanillaProvider implements ItemProvider {

    @Override
    public ItemStack getItem(String item, FlagPack pack, Player player, LootDrop drop) {
        return new ItemStack(Material.getMaterial(item.toUpperCase()));
    }

    @Override
    public boolean isPresent(String item, FlagPack pack, Player player, LootDrop drop) {
        return Material.getMaterial(item.toUpperCase())!=null;
    }


}
