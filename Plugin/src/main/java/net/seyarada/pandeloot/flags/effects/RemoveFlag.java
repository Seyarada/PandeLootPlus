package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.flags.types.IServerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityPickupItemEvent;

@FlagEffect(id="remove", description="Removes the item")
public class RemoveFlag implements IServerEvent, IEntityEvent {

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		if(!meta.getBoolean()) return;
		if(meta.trigger()==FlagTrigger.onpickup) return;
		item.remove();
	}

	@Override
	public void onCallCancellableEvent(Player p, Item i, FlagPack.FlagModifiers values, FlagTrigger trigger, Cancellable event) {
		if( !(event instanceof EntityPickupItemEvent e) ) return;
		if(!values.getBoolean()) return;

		e.setCancelled(true);
		e.getItem().remove();
	}


}
