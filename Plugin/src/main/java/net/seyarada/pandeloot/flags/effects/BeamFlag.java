package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

@FlagEffect(id="beam", description="Determines the active color of a drop")
public class BeamFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		ActiveDrop.get(item).startBeamRunnable(values.getDouble());
	}

}
