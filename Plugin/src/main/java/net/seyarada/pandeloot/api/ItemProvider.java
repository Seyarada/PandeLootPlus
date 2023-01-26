package net.seyarada.pandeloot.api;

import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.loot.Providers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemProvider {

    ItemStack getItem(String id, FlagPack pack, Player player, LootDrop drop);
    boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop);

    default void register(String... aliases) {
        for (String alias : aliases) {
            Providers.register(alias, this);
        }
    }

}
