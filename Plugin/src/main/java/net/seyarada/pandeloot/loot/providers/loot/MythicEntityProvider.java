package net.seyarada.pandeloot.loot.providers.loot;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.EntityDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;

public class MythicEntityProvider implements LootProvider {

    @Override
    public IDrop getLoot(String id, FlagPack pack, Player player, LootDrop drop) {
        return new EntityDrop(id, EntityDrop.EntityDropType.MYTHICMOBS, pack);
    }

    @Override
    public boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop) {
        return MythicBukkit.inst().getMobManager().getMythicMob(id).isPresent();
    }


}
