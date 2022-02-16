package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public interface IServerEvent extends IFlag {

	void onCallCancellableEvent(Player p, Item i, FlagPack.FlagModifiers values, FlagTrigger trigger, Cancellable event);

}
