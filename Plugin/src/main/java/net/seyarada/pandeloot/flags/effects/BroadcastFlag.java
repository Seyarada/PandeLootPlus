package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Bukkit;

@FlagEffect(id="broadcast", description="Broadcast a message")
public class BroadcastFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop iDrop, FlagTrigger trigger) {
		Bukkit.broadcastMessage(values.getString());
	}

}
