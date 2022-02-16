package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface ICondition extends IFlag {

	boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop);

	boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, @Nullable Entity entity, Player player);

}
