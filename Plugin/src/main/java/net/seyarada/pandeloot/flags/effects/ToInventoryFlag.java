package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;

@FlagEffect(id="toinventory", description="Gives the item directly to the player", priority = FlagPriority.LOW)
public class ToInventoryFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, ItemDropMeta meta) {
		if(meta.lootDrop()==null) return;
		if(meta.lootDrop().p==null) return;

		if(meta.getBoolean() && meta.lootDrop().p.getInventory().firstEmpty() >= 0) {
			meta.lootDrop().p.getInventory().addItem(item.getItemStack());
			item.remove();
		}

	}

}
