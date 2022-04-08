package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.DropMeta;
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

@FlagEffect(id="stackable", description="Determines if an item can be stacked", priority = FlagPriority.LOWEST)
public class StackableFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, DropMeta meta) {
		if(!meta.getBoolean()) {
			ItemUtils.writeData(item.getItemStack(), Constants.UNSTACKABLE_KEY, UUID.randomUUID().toString());
		}
	}

}
