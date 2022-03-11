package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.compatibility.mmoitems.MMOItemsCompatibility;
import net.seyarada.pandeloot.compatibility.mythicmobs.DropTable;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.ContainerType;
import net.seyarada.pandeloot.drops.containers.LootBag;
import net.seyarada.pandeloot.drops.containers.PredefinedDropsManager;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.conditions.ChanceFlag;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.ICondition;
import org.bukkit.Material;
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

    static IDrop getAsDrop(String str, Player player) {
        int originDiv = Math.max(str.indexOf(":")+1, 0);
        originDiv = (str.indexOf("=")>0 && str.indexOf("=")<originDiv) ? 0 : originDiv; // Make sure originDiv isn't set from a flag value
        int bracketDiv = Math.min((str.contains("{") ? str.indexOf("{") : 0), (str.contains(" ")) ? str.indexOf(" ") : str.length());
        String origin = str.substring(0, (originDiv>0) ? originDiv-1 : originDiv);
        String id = str.substring(originDiv, (bracketDiv!=0) ? bracketDiv : str.length()).split(" ")[0];

        String flagPart = str.substring(origin.length()+id.length()+1);
        FlagPack pack = FlagPack.fromCompact(flagPart);

        Logger.log("Getting as drop: origin;%s, id;%s, pack;%s, player;%s", origin, id, pack, player);

        return switch (origin) {
            case "container", "loottable", "lt" ->
                    ContainerManager.get(id);
            case "lootbag", "lb" ->
                    ((LootBag) ContainerManager.get(id, ContainerType.LOOTBAG)).getDrop(pack);
            case "droptable", "dt" ->
                    new DropTable(id, pack);
            case "entity" ->
                    new EntityDrop(id, EntityDrop.EntityDropType.VANILLA, pack);
            case "mmentity" ->
                    new EntityDrop(id, EntityDrop.EntityDropType.MYTHICMOBS, pack);
            case "i" ->
                    PredefinedDropsManager.get(id);
            default ->
                    new ItemDrop(getItem(origin, id, pack, player), pack);
        };

    }

    static ArrayList<IDrop> getAsDrop(List<String> strListDrop, Player player) {
        ArrayList<IDrop> dropList = new ArrayList<>();
        for(String str : strListDrop) {
            dropList.add(getAsDrop(str, player));
        }
        return dropList;
    }

    static ItemStack getItem(String origin, String item, FlagPack pack, Player player) {
        ItemStack iS = null;
        switch (origin) {
            case "mythicmobs", "mm" ->
                    iS = MythicMobsCompatibility.getItem(item);
            case "mmoitems", "mi" ->
                    iS = MMOItemsCompatibility.getItem(item, pack, player);
            default -> {
                Material mat = Material.getMaterial(item.toUpperCase());
                if (mat == null) break;
                iS = new ItemStack(mat);
            }
        }
        if(iS==null) iS = new ItemStack(Material.STONE);
        return iS;
    }

}
