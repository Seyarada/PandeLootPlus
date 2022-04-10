package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.types.IItemEvent;
import org.bukkit.entity.Item;

import java.util.Random;
import java.util.logging.Level;

@FlagEffect(id="amount", description="Changes the amount of a drop", priority = FlagPriority.LOW)
public class AmountFlag implements IItemEvent {

	@Override
	public void onCallItem(Item item, ItemDropMeta meta) {
		int amount = getValueFromRanged(meta.getString());
		item.getItemStack().setAmount(amount);
	}

	public static int getValueFromRanged(String strAmount) {
		if(strAmount.contains("to")) {
			final String[] n = strAmount.split("to");
			final Random r = new Random();
			if(!n[0].isEmpty() && !n[1].isEmpty()) {
				final double rangeMin = Double.parseDouble(n[0]);
				final double rangeMax = Double.parseDouble(n[1])+1;
				strAmount = String.valueOf(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
			}
		}

		try {
			return (int) Float.parseFloat(strAmount);
		} catch (NumberFormatException e) {
			Logger.log(Level.WARNING, "Invalid amount for " + strAmount);
			return 1;
		}
	}

}
