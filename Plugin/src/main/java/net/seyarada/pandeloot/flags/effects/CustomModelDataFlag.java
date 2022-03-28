package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="model", description="Changes the CustomModelData of an item")
public class CustomModelDataFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable ItemDrop itemDrop, FlagTrigger trigger) {
		if(values.getInt()>0) {
			ItemMeta meta = item.getItemStack().getItemMeta();
			if(meta!=null) {
				meta.setCustomModelData(values.getInt());
				item.getItemStack().setItemMeta(meta);
			}
		}
	}

}
