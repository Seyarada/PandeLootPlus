package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

public interface IEntityEvent extends IFlag {

	void onCallEntity(Entity entity, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger);

}
