package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.active.ItemActive;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
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

@FlagEffect(id="hologram", description="Creates an hologram for an item")
public class HologramFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity entity, ItemDropMeta meta) {
		switch (meta.getString().toLowerCase()) {
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
						attachedHologram(entity, meta.lootDrop(), iS.getItemMeta().getLore());
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
						if(meta.lootDrop()!=null) {
							for (int i = 0, fullTextSize = fullText.size(); i < fullTextSize; i++) {
								fullText.set(i, meta.lootDrop().substitutor(fullText.get(i)));
							}
						}
						attachedHologram(entity, meta.lootDrop(), fullText);
					}
				}
			}
			default -> attachedHologram(entity, meta.lootDrop(), Arrays.asList(meta.getString().split(",")));
		}

	}

	public void attachedHologram(Entity e, LootDrop drop, List<String> text) {

		if(text.size()==1 && text.get(0).length()<16) {
			e.setCustomName(text.get(0));
			e.setCustomNameVisible(true);
			return;
		}

		text.forEach(drop::substitutor);
		List<Entity> bukkitArmorStands = NMSManager.get().hologram(e.getLocation(), drop.p, text, PandeLoot.inst);
		ItemActive.get(e).startHologramRunnable(e, bukkitArmorStands, Collections.singletonList(drop.p));
	}

}
