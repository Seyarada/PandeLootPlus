package net.seyarada.pandeloot.flags.effects;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

@FlagEffect(id="color", description="Determines the active color of a drop")
public class ColorFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, DropMeta meta) {
		ActiveDrop activeDrop = ActiveDrop.get(item);

		String colorString = meta.getString();
		switch (colorString.toUpperCase()) {
			case "RANDOM" -> colorString = ColorUtils.getRandomColorString();
			case "RAINBOW" -> {
				activeDrop.startRainbowRunnable();
				return;
			}
		}

		activeDrop.setColor(ChatColor.of(colorString));
	}

}
