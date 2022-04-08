package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="toinventory", description="Gives the item directly to the player", priority = FlagPriority.LOW)
public class ToInventoryFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, DropMeta meta) {
		if(meta.lootDrop()==null) return;
		if(meta.lootDrop().p==null) return;

		if(meta.getBoolean() && meta.lootDrop().p.getInventory().firstEmpty() >= 0) {
			meta.lootDrop().p.getInventory().addItem(item.getItemStack());
			item.remove();
		}

	}

}
