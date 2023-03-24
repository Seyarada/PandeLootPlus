package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;

@FlagEffect(id="command", description="Executes a command")
public class CommandFlag implements IGeneralEvent {

	@Override
	public void onCallGeneral(ItemDropMeta meta) {
		if(meta.getString()!=null) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), meta.getString());
		} else {
			String sB =
					"Tried to execute an empty command!" + System.lineSeparator() +
					"Information: " + System.lineSeparator() +
					"- FlagPack > " + meta.iDrop().getFlagPack() + System.lineSeparator() +
					"- Player > " + meta.lootDrop().p.getDisplayName() + System.lineSeparator() +
					"- Command > " + Collections.singletonList(meta.values());
			Logger.userWarning(sB);
		}
	}

}
