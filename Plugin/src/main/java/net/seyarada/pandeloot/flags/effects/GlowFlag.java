package net.seyarada.pandeloot.flags.effects;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

@FlagEffect(id="glow", description="Gives an item to the player")
public class GlowFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop itemDrop, FlagTrigger trigger) {
		item.setGlowing(values.getBoolean());
		ActiveDrop activeDrop = ActiveDrop.get(item);
		if(!activeDrop.getColor().equals(Constants.ACCENT) && lootDrop!=null)
			ColorUtils.setItemColor(item, activeDrop.getColor(), lootDrop.p);
	}

	public void updateColor(Entity i, ChatColor color, Player player) {
		ColorUtils.setItemColor(i, color, player);
	}

}
