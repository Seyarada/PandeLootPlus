package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="title", description="Broadcast a message")
public class TitleFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		String title = values.getString();
		String subtitle = values.getOrDefault("subtitle", "");

		int titleFade = values.getIntOrDefault("fade", 10);
		int titleDuration = values.getIntOrDefault("duration", 20);

		player.sendTitle(title, subtitle, titleFade, titleDuration, titleFade);
	}

}
