package net.seyarada.pandeloot.loot;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.api.ItemProvider;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.loot.providers.item.MMOItemProvider;
import net.seyarada.pandeloot.loot.providers.item.MythicItemProvider;
import net.seyarada.pandeloot.loot.providers.item.VanillaProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ItemProviderManager {

    static final HashMap<String, ItemProvider> ITEM_PROVIDERS = new HashMap<>();

    public static void init() {
        new VanillaProvider().register("minecraft", "mc", "vanilla", "v");

        if(PandeLoot.mythicEnabled) new MythicItemProvider().register("mythicmobs", "mm");
        if(PandeLoot.mmoItemsEnabled) new MMOItemProvider().register("mmoitems", "mi");
    }

    public static ItemStack get(String origin, String id, FlagPack pack, Player player, LootDrop drop) {
        if(origin!=null && ITEM_PROVIDERS.containsKey(origin))
            return ITEM_PROVIDERS.get(origin).getItem(id, pack, player, drop);

        for (ItemProvider provider : ITEM_PROVIDERS.values()) {
            if(provider.isPresent(id, pack, player, drop))
                return provider.getItem(id, pack, player, drop);
        }

        return new ItemStack(Material.STONE);
    }

    public static void register(String alias, ItemProvider provider) {
        ITEM_PROVIDERS.put(alias, provider);
    }

}
