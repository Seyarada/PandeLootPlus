package net.seyarada.pandeloot.loot;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.api.ItemProvider;
import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.loot.providers.item.MMOItemProvider;
import net.seyarada.pandeloot.loot.providers.item.MythicItemProvider;
import net.seyarada.pandeloot.loot.providers.item.VanillaProvider;
import net.seyarada.pandeloot.loot.providers.loot.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Providers {

    static final HashMap<String, ItemProvider> ITEM_PROVIDERS = new HashMap<>();
    static final HashMap<String, LootProvider> LOOT_PROVIDERS = new HashMap<>();

    static {
        // Items
        new VanillaProvider().register("minecraft", "mc", "vanilla", "v", "item");

        if(PandeLoot.mythicEnabled) new MythicItemProvider().register("mythicmobs", "mm");
        if(PandeLoot.mmoItemsEnabled) new MMOItemProvider().register("mmoitems", "mi");

        // Entities/Special
        new LootBagProvider().register("lootbag", "lb", "bag");
        new LootTableProvider().register("loottable", "lt", "container");
        new EntityProvider().register("entity", "e");
        new PredefinedDropsProvider().register("i");

        if (PandeLoot.mythicEnabled) {
            new DropTableProvider().register("droptable", "dt", "drop");
            new MythicEntityProvider().register("mmentity", "mythicmob");
        }
    }

    public static IDrop get(String origin, String id, FlagPack pack, Player player, LootDrop drop) {
        // Sees if PandeLoot knows any item provider from that origin
        if(origin!=null && ITEM_PROVIDERS.containsKey(origin))
            return new ItemDrop(ITEM_PROVIDERS.get(origin).getItem(id, pack, player, drop), pack);

        // Sees if PandeLoot knows any entity/generic provider from that origin
        if(origin!=null && LOOT_PROVIDERS.containsKey(origin))
            return LOOT_PROVIDERS.get(origin).getLoot(id, pack, player, drop);

        // No origin providers found, so it's likely the origin wasn't specified
        // Now it does a deeper search to see if the providers know the ID

        // Sees if any item provider knows the ID
        for (ItemProvider provider : ITEM_PROVIDERS.values()) {
            if(provider.isPresent(id, pack, player, drop))
                return new ItemDrop(provider.getItem(id, pack, player, drop), pack);
        }

        // Sees if any entity/special provider knows the ID
        for (LootProvider provider : LOOT_PROVIDERS.values()) {
            if(provider.isPresent(id, pack, player, drop))
                return provider.getLoot(id, pack, player, drop);
        }

        // Couldn't find any provider so default to returning stone
        return new ItemDrop(new ItemStack(Material.STONE), pack);
    }


    public static void register(String alias, ItemProvider provider) {
        ITEM_PROVIDERS.put(alias, provider);
    }

    public static void register(String alias, LootProvider provider) {
        LOOT_PROVIDERS.put(alias, provider);
    }

}
