package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import org.bukkit.entity.Player;

public interface IPlayerEvent extends IFlag {

	void onCallPlayer(Player player, ItemDropMeta meta);

}
