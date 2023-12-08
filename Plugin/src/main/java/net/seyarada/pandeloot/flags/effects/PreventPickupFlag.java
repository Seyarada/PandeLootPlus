package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;

@FlagEffect(id="preventpickup", description="Prevents item pickup")
public class PreventPickupFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, ItemDropMeta meta) {
		ActiveDrop.get(item).canBePickedUp = !meta.getBoolean();
	}

}
