package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.ItemDropMeta;

public interface IGeneralEvent extends IFlag {

	void onCallGeneral(ItemDropMeta meta);

}
