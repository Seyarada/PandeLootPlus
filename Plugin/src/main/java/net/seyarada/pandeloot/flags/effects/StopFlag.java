package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

import javax.annotation.Nullable;

@FlagEffect(id="stop", description="Determines the active color of a drop")
public class StopFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		if(lootDrop!=null) lootDrop.continueDrops = false;
	}

}
