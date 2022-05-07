package net.seyarada.pandeloot.loot.providers.loot;

import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.PredefinedDropsManager;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;

public class PredefinedDropsProvider implements LootProvider {

    @Override
    public IDrop getLoot(String id, FlagPack pack, Player player, LootDrop drop) {
        return PredefinedDropsManager.get(id);
    }

    @Override
    public boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop) {
        return PredefinedDropsManager.get(id)!=null;
    }

}
