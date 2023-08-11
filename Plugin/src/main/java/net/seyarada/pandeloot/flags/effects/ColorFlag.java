package net.seyarada.pandeloot.flags.effects;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.drops.active.ItemActive;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;

@FlagEffect(id="color", description="Determines the active color of a drop")
public class ColorFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity entity, ItemDropMeta meta) {
		ItemActive activeDrop = ItemActive.get(entity);

		String colorString = meta.getString();
		switch (colorString.toUpperCase()) {
			case "RANDOM" -> colorString = ColorUtils.getRandomColorString();
			case "RAINBOW" -> {
				activeDrop.startRainbowRunnable(meta.getIntOrDefault("frequency", 3));
				return;
			}
			case "MATCH", "DISPLAY" -> {
				if(entity instanceof Item item) {
					ItemMeta itemMeta = item.getItemStack().getItemMeta();
					if(itemMeta!=null && itemMeta.hasDisplayName()) {
						String colorSymbol = itemMeta.getDisplayName().substring(1, 2);
						colorString = ColorUtils.findStringColor(colorSymbol);
					} else return;
				} else return;
			}
		}

		activeDrop.setColor(ChatColor.of(colorString));
	}

}
