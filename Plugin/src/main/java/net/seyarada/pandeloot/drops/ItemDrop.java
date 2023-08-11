package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.active.ItemActive;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ItemDrop implements IDrop {

    public final ItemStack item;
    public final FlagPack pack;
    public final HashMap<NamespacedKey, String> data = new HashMap<>();

    public ItemDrop(ItemStack item, FlagPack pack) {
        this.item = item;
        this.pack = pack;
        Logger.log("Generated ItemDrop with %s and %s", item, pack);
    }

    @Override
    public void run(LootDrop lootDrop) {
        Location dropLocation = lootDrop.getLocation();
        if (dropLocation == null) {
            Logger.userWarning("Unable to find where to drop %s", this);
            return;
        }

        getFlagPack().trigger(FlagTrigger.onprespawn, null, lootDrop, this);
        if(item.getType() != Material.AIR) {
            dropLocation.getWorld().dropItemNaturally(dropLocation, item, i -> {
                for(Map.Entry<NamespacedKey, String> entry : data.entrySet()) {
                    i.getPersistentDataContainer().set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
                }
                new ItemActive(this, i, lootDrop.p, pack, lootDrop);
            });
        } else {
            new ItemActive(this, null, lootDrop.p, pack, lootDrop);
        }


    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public FlagPack getFlagPack() {
        return pack;
    }


}
