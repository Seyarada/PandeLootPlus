package net.seyarada.pandeloot.loot.providers.loot;

import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;

public class LootTableProvider implements LootProvider {

    @Override
    public IDrop getLoot(String item, FlagPack pack, Player player, LootDrop drop) {
        return ContainerManager.get(item);
    }

    @Override
    public boolean isPresent(String item, FlagPack pack, Player player, LootDrop drop) {
        return ContainerManager.get(item)!=null;
    }


}
