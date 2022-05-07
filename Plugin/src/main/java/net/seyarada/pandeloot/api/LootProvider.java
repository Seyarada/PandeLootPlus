package net.seyarada.pandeloot.api;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.loot.LootProviderManager;
import org.bukkit.entity.Player;

public interface LootProvider {

    IDrop getLoot(String id, FlagPack pack, Player player, LootDrop drop);
    boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop);

    default void register(String... aliases) {
        for (String alias : aliases) {
            LootProviderManager.register(alias, this);
        }
    }

}
