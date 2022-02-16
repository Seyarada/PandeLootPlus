package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="delay", description="Determines the active color of a drop")
public class DelayFlag implements IGeneralEvent {


	@Override
	public void onCallGeneral(FlagPack.FlagModifiers values, @org.jetbrains.annotations.Nullable LootDrop lootDrop, @org.jetbrains.annotations.Nullable IDrop iDrop, FlagTrigger trigger) {

	}

}
