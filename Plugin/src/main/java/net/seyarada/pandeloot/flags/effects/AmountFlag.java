package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.logging.Level;

@FlagEffect(id="amount", description="Broadcast a message", priority = FlagPriority.LOW)
public class AmountFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable ItemDrop itemDrop, FlagTrigger trigger) {

		String strAmount = values.getString();

		if(strAmount.contains("to")) {
			final String[] n = strAmount.split("to");
			final Random r = new Random();
			if(!n[0].isEmpty() && !n[1].isEmpty()) {
				final double rangeMin = Double.parseDouble(n[0]);
				final double rangeMax = Double.parseDouble(n[1]);
				strAmount = String.valueOf(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
			}
		}

		try {
			int amount = Integer.parseInt(strAmount);
			item.getItemStack().setAmount(amount);
		} catch (NumberFormatException e) {
			Logger.log(Level.WARNING, "Invalid amount for "+values.getString());
		}
	}

}
