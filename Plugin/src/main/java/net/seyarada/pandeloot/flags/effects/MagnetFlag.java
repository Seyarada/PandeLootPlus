package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.active.ItemActive;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

@FlagEffect(id="magnet", description="Make drops get attracted to the player")
public class MagnetFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		int delay = meta.getIntOrDefault("delay", 40);
		double distanceTrigger = meta.getDoubleOrDefault("distance", 16);

		if(delay>0) {
			Bukkit.getScheduler().runTaskLater(PandeLoot.inst, () -> {
				ItemActive.get(item).startMagnetRunnable(meta.getDouble()/10, distanceTrigger, meta.getIntOrDefault("frequency", 1));
			}, delay);
		} else ItemActive.get(item).startMagnetRunnable(meta.getDouble()/10, distanceTrigger, meta.getIntOrDefault("frequency", 1));
	}

}
