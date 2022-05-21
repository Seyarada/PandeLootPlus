package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if(Config.blockBreak() && e.getPlayer().getGameMode()==GameMode.SURVIVAL) {
            e.setDropItems(false);
            FlagPack pack = FlagPack.fromCompact(Config.blockBreakFlags());
            e.getBlock().getDrops(e.getPlayer().getItemInHand(), e.getPlayer()).forEach(iS -> {
                ItemDrop itemDrop = new ItemDrop(iS, pack);
                new LootDrop(itemDrop, e.getPlayer(), e.getBlock().getLocation())
                        .build()
                        .drop();
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if(Config.playerDrop()) {
            FlagPack pack = FlagPack.fromCompact(Config.playerDropFlags());
            EntityDrop eDrop = new EntityDrop(e.getItemDrop(), pack);
            new LootDrop(eDrop, e.getPlayer(), e.getPlayer().getLocation())
                    .build()
                    .drop();
        }
    }

}
