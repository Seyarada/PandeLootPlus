package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="experience", description="Gives experience points to a player")
public class ExperienceFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, DropMeta meta) {
		player.giveExp(meta.getInt());
	}

}
