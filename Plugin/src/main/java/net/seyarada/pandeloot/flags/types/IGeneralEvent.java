package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;

public interface IGeneralEvent extends IFlag {

	void onCallGeneral(DropMeta meta);

}
