package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import org.bukkit.entity.Entity;

@FlagEffect(id="voidprotection", description="Protects an item from falling to the void")
public class VoidProtectionFlag implements IEntityEvent {

	// The prevent drop form lootbag won't work if the lootbag item is a tile entity!

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		ActiveDrop activeDrop = ActiveDrop.get(item);
		int frequency = meta.getIntOrDefault("frequency", 20);
		activeDrop.startVoidProtectionRunnable(meta.getDouble(), frequency);
	}

}

