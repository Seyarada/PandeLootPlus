package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="stop", description="Stops the drop execution")
public class StopFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(ItemDropMeta meta) {
		if(meta.lootDrop()!=null) meta.lootDrop().continueDrops = false;
	}

}
