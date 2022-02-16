package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@FlagEffect(id="visibility", description="Determines the active color of a drop")
public class VisibilityFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		if(lootDrop==null) return;

		switch (values.getString().toLowerCase()) {
			default -> {
				if(lootDrop.p==null) return;
				List<UUID> players = Collections.singletonList(lootDrop.p.getUniqueId());
				NMSManager.addHiddenItem(item.getEntityId(), players);
			}
			case "party" -> {
				if(lootDrop.damageBoard==null) return;
				List<UUID> players = lootDrop.damageBoard.playerRanks;
				NMSManager.addHiddenItem(item.getEntityId(), players);
			}
		}
	}

	/*
	void hideEntity(Entity toHide, List<Player> canView) {
		for (Entity entity : toHide.getNearbyEntities(42, 42, 42)) {
			if (entity instanceof Player && !canView.contains(entity)) {
				NMSManager.destroyEntity(toHide.getEntityId(), entity);
			}
		}
	}

	 */

}
