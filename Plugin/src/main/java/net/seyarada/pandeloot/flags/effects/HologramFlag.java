package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.nms.NMSManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@FlagEffect(id="hologram", description="Gives an item to the player")
public class HologramFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity entity, FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop, FlagTrigger trigger) {

		switch (values.getString().toLowerCase()) {
			case "displaycustom" -> {
				if(entity instanceof Item item) {
					ItemStack iS = item.getItemStack();
					if(iS.hasItemMeta()) {
						item.setCustomName(iS.getItemMeta().getDisplayName());
						item.setCustomNameVisible(true);
					}
				}
			}
			case "display" -> {
				if(entity instanceof Item item) {
					ItemStack iS = item.getItemStack();
					String customName = (iS.hasItemMeta())
							? iS.getItemMeta().getDisplayName()
							: WordUtils.capitalizeFully(iS.getType().toString().replaceAll("_", " "));
					item.setCustomName(customName);
					item.setCustomNameVisible(true);
				}
			}
			case "lore" -> {
				if(entity instanceof Item item) {
					ItemStack iS = item.getItemStack();
					if(iS.hasItemMeta()) {
						attachedHologram(entity, lootDrop, iS.getItemMeta().getLore());
					}
				}
			}
			case "full" -> {
				if(entity instanceof Item item) {
					ItemStack iS = item.getItemStack();
					if(iS.hasItemMeta()) {
						List<String> fullText = iS.getItemMeta().getLore();
						if(fullText==null) fullText = new ArrayList<>();
						fullText.add(0, (iS.hasItemMeta())
								? iS.getItemMeta().getDisplayName()
								: WordUtils.capitalizeFully(iS.getType().toString().replaceAll("_", " ")));
						if(lootDrop!=null) {
							for (int i = 0, fullTextSize = fullText.size(); i < fullTextSize; i++) {
								fullText.set(i, lootDrop.parse(fullText.get(i)));
							}
						}
						attachedHologram(entity, lootDrop, fullText);
					}
				}
			}
			default -> attachedHologram(entity, lootDrop, Arrays.asList(values.getString().split(",")));
		}

	}

	public void attachedHologram(Entity e, LootDrop drop, List<String> text) {
		text.forEach(t -> t = drop.parse(t));
		List<Entity> bukkitArmorStands = NMSManager.get().hologram(0, e.getLocation(), drop.p, text, PandeLoot.inst);
		ActiveDrop.get(e).startHologramRunnable(e, bukkitArmorStands, Collections.singletonList(drop.p));
	}

}
