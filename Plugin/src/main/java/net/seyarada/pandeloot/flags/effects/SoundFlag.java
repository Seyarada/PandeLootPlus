package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;

@FlagEffect(id="sound", description="Plays a sound")
public class SoundFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		float volume = (float) meta.getDoubleOrDefault("volume", 1);
		float pitch = (float) meta.getDoubleOrDefault("pitch", 1);

		player.playSound(player.getLocation(), meta.getString(), volume, pitch);
	}

}
