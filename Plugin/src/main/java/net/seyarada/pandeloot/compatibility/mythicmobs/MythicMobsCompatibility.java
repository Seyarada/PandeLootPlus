package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MythicMobsCompatibility {

    public static ItemStack getItem(String item) {
        Optional<MythicItem> mI = MythicMobs.inst().getItemManager().getItem(item);
        return mI.map(mythicItem -> BukkitAdapter.adapt(mythicItem.generateItemStack(1))).orElse(null);
    }




}
