package net.seyarada.pandeloot.compatibility.mmocore;

import net.Indyuce.mmocore.api.event.CustomBlockMineEvent;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MMOCoreCompatibility implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDrop(CustomBlockMineEvent e) {
        if(Config.blockBreakMMO()) {
            FlagPack pack = FlagPack.fromCompact(Config.blockBreakMMOFlags());
            e.getDrops().forEach(iS -> {
                ItemDrop itemDrop = new ItemDrop(iS, pack);
                new LootDrop(itemDrop, e.getPlayer(), e.getBlock().getLocation())
                        .build()
                        .drop();
            });
        }
    }

}
