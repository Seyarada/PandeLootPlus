package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.DropMeta;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@FlagEffect(id="consumeitem", description="Takes certain amount of the held item")
public class ConsumeItemFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, DropMeta meta) {
		ItemStack iS = player.getInventory().getItemInMainHand();
		iS.setAmount(iS.getAmount()-meta.getInt());
	}

}
