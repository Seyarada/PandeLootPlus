package net.seyarada.pandeloot.loot.providers.loot;

import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.EntityDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntityProvider implements LootProvider {

    @Override
    public IDrop getLoot(String id, FlagPack pack, Player player, LootDrop drop) {
        return new EntityDrop(id, EntityDrop.EntityDropType.VANILLA, pack);
    }

    @Override
    public boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop) {
        try {
            EntityType.valueOf(id.toUpperCase());
            return true;
        } catch (IllegalArgumentException exp){
            return false;
        }
    }


}
