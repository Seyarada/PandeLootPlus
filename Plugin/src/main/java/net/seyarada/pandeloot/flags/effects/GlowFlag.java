package net.seyarada.pandeloot.flags.effects;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@FlagEffect(id="glow", description="Makes an item glow")
public class GlowFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		item.setGlowing(meta.getBoolean());
		ActiveDrop activeDrop = ActiveDrop.get(item);
		if(!activeDrop.getColor().equals(Constants.ACCENT) && meta.lootDrop()!=null)
			ColorUtils.setItemColor(item, activeDrop.getColor(), meta.lootDrop().p);
	}

	public void updateColor(Entity i, ChatColor color, Player player) {
		ColorUtils.setItemColor(i, color, player);
	}

}
