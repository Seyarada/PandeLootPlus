package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;

@FlagEffect(id="message", description="Sends a message to a player")
public class MessageFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		player.sendMessage(meta.getString());
	}

}
