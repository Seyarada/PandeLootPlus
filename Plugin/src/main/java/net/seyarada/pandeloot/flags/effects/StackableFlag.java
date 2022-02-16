package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@FlagEffect(id="stackable", description="Broadcast a message", priority = FlagPriority.LOWEST)
public class StackableFlag implements IItemEvent {

	// TODO: Change this to merge cancel event in ActiveDrop

	@Override
	public void onCallItem(Item item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable ItemDrop itemDrop, FlagTrigger trigger) {
		if(!values.getBoolean()) {
			ItemUtils.writeData(item.getItemStack(), Constants.UNSTACKABLE_KEY, UUID.randomUUID().toString());
		}
	}

}
