package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="type", description="Determines the MMOItems type")
public class TypeFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(ItemDropMeta meta) {

	}

}
