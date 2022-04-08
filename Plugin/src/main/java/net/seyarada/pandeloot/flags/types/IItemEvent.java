package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.entity.Item;

import javax.annotation.Nullable;

public interface IItemEvent extends IFlag {

	void onCallItem(Item item, DropMeta meta);

}
