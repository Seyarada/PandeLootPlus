package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="delay", description="Determines the delay of the drop")
public class DelayFlag implements IGeneralEvent {


	@Override
	public void onCallGeneral(DropMeta meta) {

	}

}
