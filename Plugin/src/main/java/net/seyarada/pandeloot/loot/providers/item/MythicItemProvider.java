package net.seyarada.pandeloot.loot.providers.item;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import net.seyarada.pandeloot.api.ItemProvider;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MythicItemProvider implements ItemProvider {

    @Override
    public ItemStack getItem(String item, FlagPack pack, Player player, LootDrop drop) {
        Optional<MythicItem> mI = MythicBukkit.inst().getItemManager().getItem(item);
        return mI.map(mythicItem -> BukkitAdapter.adapt(mythicItem.generateItemStack(1))).orElse(null);
    }

    @Override
    public boolean isPresent(String item, FlagPack pack, Player player, LootDrop drop) {
        return MythicBukkit.inst().getItemManager().getItem(item).isPresent();
    }


}
