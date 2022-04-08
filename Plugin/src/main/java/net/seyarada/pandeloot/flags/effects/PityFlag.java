package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.config.Pity;
import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@FlagEffect(id="pity", description="Manages the Pity system")
public class PityFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, DropMeta meta) {
		HashMap<String, Double> pity = Pity.pity.getOrDefault(player.getUniqueId().toString(), new HashMap<>());
		String id = meta.getOrDefault("id", "Global");
		double newPity = pity.getOrDefault(id, 0d) + meta.getDouble();
		pity.put(id, newPity);
		Pity.pity.put(player.getUniqueId().toString(), pity);
	}

}
