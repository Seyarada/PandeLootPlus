package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class MythicMobsCompatibility {

    public static ItemStack getItem(String item) {
        Optional<MythicItem> mI = MythicBukkit.inst().getItemManager().getItem(item);
        return mI.map(mythicItem -> BukkitAdapter.adapt(mythicItem.generateItemStack(1))).orElse(null);
    }




}
