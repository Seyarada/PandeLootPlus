package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Bukkit;

@FlagEffect(id="command", description="Executes a command")
public class CommandFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(ItemDropMeta meta) {
		if(meta.getString()!=null) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), meta.getString());
		}
	}

}
