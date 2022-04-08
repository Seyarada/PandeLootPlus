package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Bukkit;

@FlagEffect(id="command", description="Executes a command")
public class CommandFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(DropMeta meta) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), meta.getString());
	}

}
