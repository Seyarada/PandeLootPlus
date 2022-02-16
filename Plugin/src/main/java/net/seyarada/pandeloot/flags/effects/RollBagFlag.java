package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

@FlagEffect(id="rollbag", description="Determines the active color of a drop")
public class RollBagFlag implements IEntityEvent {

	// The prevent drop form lootbag won't work if the lootbag item is a tile entity!

	@Override
	public void onCallEntity(Entity item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		ActiveDrop activeDrop = ActiveDrop.get(item);

		if(values.getBoolean()) {
			if(iDrop==null) return;
			IContainer lootBag = ContainerManager.get(((ItemDrop) iDrop).data.get(Constants.LOOTBAG_KEY));
			activeDrop.startLootBagRollRunnable(lootBag, lootDrop, iDrop.getFlagPack());
		} else {
			activeDrop.stopLootBagRunnable();
		}

	}

}

