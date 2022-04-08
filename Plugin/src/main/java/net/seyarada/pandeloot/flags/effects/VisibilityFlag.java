package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
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

@FlagEffect(id="visibility", description="Changes the visibility of a drop")
public class VisibilityFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, DropMeta meta) {
		if(meta.lootDrop()==null) return;

		switch (meta.getString().toLowerCase()) {
			default -> {
				if(meta.lootDrop().p==null) return;
				List<UUID> players = Collections.singletonList(meta.lootDrop().p.getUniqueId());
				NMSManager.addHiddenItem(item.getEntityId(), players);
			}
			case "party" -> {
				if(meta.lootDrop().damageBoard==null) return;
				List<UUID> players = meta.lootDrop().damageBoard.playerRanks;
				NMSManager.addHiddenItem(item.getEntityId(), players);
			}
		}
	}

}
