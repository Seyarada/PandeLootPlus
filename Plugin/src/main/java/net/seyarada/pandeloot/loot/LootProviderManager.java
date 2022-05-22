package net.seyarada.pandeloot.loot;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.api.LootProvider;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.loot.providers.loot.*;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static net.seyarada.pandeloot.loot.ItemProviderManager.ITEM_PROVIDERS;

public class LootProviderManager {

    static final HashMap<String, LootProvider> LOOT_PROVIDERS = new HashMap<>();

    public static void init() {
        new LootTableProvider().register("loottable", "lt", "container");
        new LootBagProvider().register("lootbag", "lb", "bag");
        new EntityProvider().register("entity", "e");
        new PredefinedDropsProvider().register("i");

        if (PandeLoot.mythicEnabled) {
            new DropTableProvider().register("droptable", "dt", "drop");
            new MythicEntityProvider().register("mmentity", "mythicmob");
        }
    }

    public static IDrop get(String origin, String id, FlagPack pack, Player player, LootDrop drop) {
        if(origin!=null && LOOT_PROVIDERS.containsKey(origin))
            return LOOT_PROVIDERS.get(origin).getLoot(id, pack, player, drop);
        if(origin!=null && ITEM_PROVIDERS.containsKey(origin))
            return new ItemDrop(IDrop.getItem(origin, id, pack, player, drop), pack);

        for (LootProvider provider : LOOT_PROVIDERS.values()) {
            if(provider.isPresent(id, pack, player, drop))
                return provider.getLoot(id, pack, player, drop);
        }
        return new ItemDrop(IDrop.getItem(origin, id, pack, player, drop), pack);
    }

    public static void register(String alias, LootProvider provider) {
        LOOT_PROVIDERS.put(alias, provider);
    }

}
