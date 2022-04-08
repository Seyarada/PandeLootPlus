package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface IPlayerEvent extends IFlag {

	void onCallPlayer(Player player, DropMeta meta);

}
