package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.conditions.ChanceFlag;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.ICondition;
import net.seyarada.pandeloot.loot.Providers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IDrop {

    FlagPack getFlagPack();

    void run(LootDrop lootDrop);

    default double getChance(LootDrop drop) {
        HashMap<ICondition, FlagPack.FlagModifiers> onSpawnConds = getFlagPack().conditionFlags.get(FlagTrigger.onspawn);
        if(onSpawnConds==null) return 1;

        ChanceFlag chanceFlag = (ChanceFlag) FlagManager.getFromID("chance");
        if(onSpawnConds.containsKey(chanceFlag)) {
            FlagPack.FlagModifiers modifiers = onSpawnConds.get(chanceFlag);
            return ChanceFlag.getChance(modifiers, drop);
        }

        return 1;
    }

    default boolean passesConditions(LootDrop lootDrop, ICondition... ignore) {
        List<ICondition> ignoreConditions = List.of(ignore);
        if(!getFlagPack().conditionFlags.containsKey(FlagTrigger.onspawn)) return true;
        for(Map.Entry<ICondition, FlagPack.FlagModifiers> entry : getFlagPack().conditionFlags.get(FlagTrigger.onspawn).entrySet()) {
            ICondition condition = entry.getKey();
            if(ignoreConditions.contains(condition)) continue;
            FlagPack.FlagModifiers values = entry.getValue();

            boolean result = !condition.onCheck(values, lootDrop, this);
            if(values.getBoolean("invert")) result = !result;
            if(result) return false;

        }
        return true;
    }

    default boolean passesCondition(LootDrop lootDrop, ICondition condition) {
        if(!getFlagPack().conditionFlags.containsKey(FlagTrigger.onspawn)) return true;
        if(getFlagPack().conditionFlags.get(FlagTrigger.onspawn).containsKey(condition)) {
            FlagPack.FlagModifiers values = getFlagPack().conditionFlags.get(FlagTrigger.onspawn).get(condition);
            boolean result = condition.onCheck(values, lootDrop, this);
            if(values.getBoolean("invert")) return !result;
            return result;
        }
        return true;
    }

    ItemStack getItemStack();

    static IDrop getAsDrop(String str, Player player, LootDrop drop) {
        FlagPack pack = FlagPack.fromCompact(str);

        String origin = pack.flagString.origin;
        String id = pack.flagString.item;

        Logger.log("Getting as drop: origin;%s, id;%s, pack;%s, player;%s", origin, id, pack, player);

        return Providers.get(origin, id, pack, player, drop);

    }

    static ArrayList<IDrop> getAsDrop(List<String> strListDrop, Player player, LootDrop drop) {
        ArrayList<IDrop> dropList = new ArrayList<>();
        for(String str : strListDrop) {
            dropList.add(getAsDrop(str, player, drop));
        }
        return dropList;
    }

    static ItemStack getItem(String origin, String item, FlagPack pack, Player player, LootDrop drop) {
        return Providers.get(origin, item, pack, player, drop).getItemStack();
    }

}
