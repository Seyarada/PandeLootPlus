package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="delay", description="Determines the delay of the drop")
public class DelayFlag implements IGeneralEvent {


	@Override
	public void onCallGeneral(ItemDropMeta meta) {

	}

}
