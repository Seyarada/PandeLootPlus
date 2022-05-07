package net.seyarada.pandeloot.loot.providers.loot;

import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.ContainerType;
import net.seyarada.pandeloot.drops.containers.LootBag;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;

public class LootBagProvider implements LootProvider {

    @Override
    public IDrop getLoot(String item, FlagPack pack, Player player, LootDrop drop) {
        return ((LootBag) ContainerManager.get(item, ContainerType.LOOTBAG)).getDrop(pack);
    }

    @Override
    public boolean isPresent(String item, FlagPack pack, Player player, LootDrop drop) {
        return ContainerManager.get(item, ContainerType.LOOTBAG)!=null;
    }


}
