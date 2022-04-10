package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FlagEffect(id="consumeitem", description="Takes certain amount of the held item")
public class ConsumeItemFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		ItemStack iS = player.getInventory().getItemInMainHand();
		iS.setAmount(iS.getAmount()-meta.getInt());
	}

}
