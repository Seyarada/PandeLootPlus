package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import org.bukkit.entity.Entity;

@FlagEffect(id="rollbag", description="Rolls through the items of a lootbag")
public class RollBagFlag implements IEntityEvent {

	// The prevent drop form lootbag won't work if the lootbag item is a tile entity!

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		ActiveDrop activeDrop = ActiveDrop.get(item);

		if(meta.getBoolean()) {
			if(meta.iDrop()==null) return;
			IContainer lootBag = ContainerManager.get(((ItemDrop) meta.iDrop()).data.get(Constants.LOOTBAG_KEY));
			activeDrop.startLootBagRollRunnable(lootBag, meta.lootDrop(), meta.iDrop().getFlagPack(), meta.getBooleanOrDefault("trigger", false));
		} else {
			activeDrop.stopLootBagRunnable();
		}

	}

}

