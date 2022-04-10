package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.entity.Item;

import java.util.UUID;

@FlagEffect(id="stackable", description="Determines if an item can be stacked", priority = FlagPriority.LOWEST)
public class StackableFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, ItemDropMeta meta) {
		if(!meta.getBoolean()) {
			ItemUtils.writeData(item.getItemStack(), Constants.UNSTACKABLE_KEY, UUID.randomUUID().toString());
		}
	}

}
