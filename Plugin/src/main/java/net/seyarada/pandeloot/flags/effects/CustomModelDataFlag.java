package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;

@FlagEffect(id="model", description="Changes the CustomModelData of an item")
public class CustomModelDataFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, ItemDropMeta meta) {
		if(meta.getInt()>0) {
			ItemMeta itemMeta = item.getItemStack().getItemMeta();
			if(itemMeta!=null) {
				itemMeta.setCustomModelData(meta.getInt());
				item.getItemStack().setItemMeta(itemMeta);
			}
		}
	}

}
