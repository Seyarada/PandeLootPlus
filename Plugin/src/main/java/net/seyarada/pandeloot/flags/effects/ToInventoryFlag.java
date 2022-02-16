package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="toinventory", description="Broadcast a message", priority = FlagPriority.LOW)
public class ToInventoryFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable ItemDrop itemDrop, FlagTrigger trigger) {
		if(lootDrop==null) return;
		if(lootDrop.p==null) return;

		if(values.getBoolean() && lootDrop.p.getInventory().firstEmpty() >= 0) {
			lootDrop.p.getInventory().addItem(item.getItemStack());
			item.remove();
		}

	}

}
