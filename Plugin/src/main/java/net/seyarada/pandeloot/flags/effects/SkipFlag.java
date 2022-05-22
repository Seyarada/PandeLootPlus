package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

@FlagEffect(id="skip", description="Skips x following items when on a lootdrop")
public class SkipFlag implements IGeneralEvent {


	@Override
	public void onCallGeneral(ItemDropMeta meta) {

	}

}
