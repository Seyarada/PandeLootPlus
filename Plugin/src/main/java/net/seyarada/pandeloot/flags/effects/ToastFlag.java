package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IPlayerEvent;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FlagEffect(id="toast", description="Sends a toast")
public class ToastFlag implements IPlayerEvent {

	@Override
	public void onCallPlayer(Player player, ItemDropMeta meta) {
		NMSManager.get().
				displayToast(player, meta.getString(), meta.getString("frame"),
						new ItemStack(Material.valueOf(meta.getString("icon"))));
	}

}
