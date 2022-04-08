package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="preventpickup", description="Prevents item pickup")
public class PreventPickupFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, DropMeta meta) {
		ActiveDrop.get(item).canBePickedUp = !meta.getBoolean();
	}

}
