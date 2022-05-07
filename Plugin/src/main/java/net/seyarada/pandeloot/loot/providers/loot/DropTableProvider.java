package net.seyarada.pandeloot.loot.providers.loot;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.compatibility.mythicmobs.DropTableCompatibility;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Player;

public class DropTableProvider implements LootProvider {

    @Override
    public IDrop getLoot(String id, FlagPack pack, Player player, LootDrop drop) {
        return new DropTableCompatibility(id, pack);
    }

    @Override
    public boolean isPresent(String id, FlagPack pack, Player player, LootDrop drop) {
        return MythicBukkit.inst().getDropManager().getDropTable(id).isPresent();
    }

}
