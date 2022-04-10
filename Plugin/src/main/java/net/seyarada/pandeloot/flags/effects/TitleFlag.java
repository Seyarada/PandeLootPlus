package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;

@FlagEffect(id="title", description="Sends a title")
public class TitleFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		String title = meta.getString();
		String subtitle = meta.getOrDefault("subtitle", "");

		int titleFade = meta.getIntOrDefault("fade", 10);
		int titleDuration = meta.getIntOrDefault("duration", 20);

		player.sendTitle(title, subtitle, titleFade, titleDuration, titleFade);
	}

}
