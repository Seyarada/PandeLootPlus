package net.seyarada.pandeloot.drops.containers;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.conditions.ChanceFlag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LootTable implements IContainer {

    public ArrayList<IDrop> dropList = new ArrayList<>();
    public final ConfigurationSection config;
    static ChanceFlag chanceFlag = (ChanceFlag) FlagManager.getFromID("chance");

    int totalItems;
    int minItems;
    int maxItems;

    public FlagPack flagPack = new FlagPack();

    public LootTable(ConfigurationSection config) {
        this.config = config;
    }

    public LootTable withPack(FlagPack pack) {
        flagPack = pack;
        pack.merge(Config.DEFAULT_FLAGS);
        return this;
    }

    @Override
    public void load() {
        if (config.contains("Rewards")) {
            dropList = IDrop.getAsDrop(config.getStringList("Rewards"), null, null);
        }
        minItems = Math.max(Math.min(config.getInt("Guaranteed"), dropList.size()), config.getInt("MinItems"));
        totalItems = Math.min(config.getInt("TotalItems"), dropList.size());
        maxItems = Math.min(config.getInt("MaxItems"), dropList.size());
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public List<IDrop> getDropList(LootDrop lootDrop) {
        if(lootDrop==null) return dropList;

        ArrayList<IDrop> drops = new ArrayList<>();
        ArrayList<IDrop> reserve = new ArrayList<>();

        // Removes all the drops that the player shouldn't be able to get
        ArrayList<IDrop> potentialDrops = dropList.stream()
                .filter(iDrop -> iDrop.passesConditions(lootDrop, chanceFlag))
                .collect(Collectors.toCollection(ArrayList::new));

        maxItems = Math.min(maxItems, potentialDrops.size());
        totalItems = Math.min(totalItems, potentialDrops.size());
        minItems = Math.min(minItems, potentialDrops.size());

        // Sorts drop by passing chance or not
        for (IDrop iDrop : potentialDrops) {
            if(iDrop.passesCondition(lootDrop, chanceFlag))
                drops.add(iDrop);
            else
                reserve.add(iDrop);
        }

        int minGoal = Math.max(minItems, totalItems);
        while(drops.size()<minGoal && reserve.size()>0) {
            drops.add(doRoll(lootDrop, reserve));
        }

        int maxGoal = Math.max(maxItems, totalItems);
        while(maxGoal>0 && drops.size()>maxGoal) {
            drops.remove((int)( Math.random() * drops.size() ));
        }

        return drops;
    }

    IDrop doRoll(LootDrop lootDrop, List<IDrop> items) {
        if(items.size()==0) return null;

        // Compute the total weight of all items together.
        IDrop[] rollItems = items.toArray(new IDrop[0]);
        double totalWeight = 0.0;
        for (IDrop i : items) {
            totalWeight += i.getChance(lootDrop);
        }

        // Now choose a random item.
        int idx = 0;
        for (double r = Math.random() * totalWeight; idx < rollItems.length - 1; ++idx) {
            r -= rollItems[idx].getChance(lootDrop);
            if (r <= 0.0) break;
        }

        items.remove(idx); // Makes sure there isn't any repeated
        return rollItems[idx];
    }

    @Override
    public FlagPack getFlagPack() {
        return flagPack;
    }

    @Override
    public ItemStack getItemStack() {
        Logger.userWarning("Impossible getItemStack from: "+getConfig().getName());
        return null;
    }

}
