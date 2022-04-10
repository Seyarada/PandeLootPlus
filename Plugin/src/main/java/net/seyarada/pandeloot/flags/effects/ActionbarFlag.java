package net.seyarada.pandeloot.flags.effects;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;

@FlagEffect(id="actionbar", description="Sends an action bar to the player")
public class ActionbarFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(meta.getString()));
	}

}
