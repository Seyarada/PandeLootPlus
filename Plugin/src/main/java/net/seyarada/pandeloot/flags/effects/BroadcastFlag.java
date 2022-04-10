package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Bukkit;

@FlagEffect(id="broadcast", description="Broadcast a message")
public class BroadcastFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(ItemDropMeta meta) {
		Bukkit.broadcastMessage(meta.getString());
	}

}
