package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import org.bukkit.entity.Item;

public interface IItemEvent extends IFlag {

	void onCallItem(Item item, ItemDropMeta meta);

}
