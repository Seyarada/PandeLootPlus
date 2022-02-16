package net.seyarada.pandeloot.drops.containers;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.conditions.ChanceFlag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class LootTable implements IContainer {

    public ArrayList<IDrop> dropList = new ArrayList<>();
    public final ConfigurationSection config;
    static ChanceFlag chanceFlag = (ChanceFlag) FlagManager.getFromID("chance");

    int totalItems;
    int minItems;
    int maxItems;
    int guaranteedItems;
    int goalAmount;

    public LootTable(ConfigurationSection config) {
        if (config.contains("Rewards")) {
            dropList = IDrop.getAsDrop(config.getStringList("Rewards"));
        }
        this.config = config;
        guaranteedItems = Math.min(config.getInt("Guaranteed"), dropList.size());
        totalItems = Math.min(config.getInt("TotalItems"), dropList.size());
        minItems = Math.min(config.getInt("MinItems"), dropList.size());
        maxItems = Math.min(config.getInt("MaxItems"), dropList.size());
        goalAmount = Math.max(totalItems, Math.max(minItems, maxItems));
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public List<IDrop> getDropList(LootDrop lootDrop) {
        if(lootDrop==null) return dropList;

        ArrayList<IDrop> canObtain = dropList.stream()
                .filter(iDrop -> iDrop.passesConditions(lootDrop, chanceFlag))
                .collect(Collectors.toCollection(ArrayList::new));

        goalAmount = Math.min(goalAmount, canObtain.size());
        guaranteedItems = Math.min(guaranteedItems, canObtain.size());

        ArrayList<IDrop> toGive = new ArrayList<>();

        if(goalAmount>0) {
            for(int i = 0; i<goalAmount; i++)
                toGive.add(doRoll(lootDrop, canObtain));
            return toGive;
        }

        for(IDrop iDrop : canObtain)
            if(iDrop.passesCondition(lootDrop, chanceFlag))
                toGive.add(iDrop);
        canObtain.removeAll(toGive);

        while (canObtain.size()>0 && toGive.size()<guaranteedItems)
                toGive.add(doRoll(lootDrop, canObtain));

        return toGive;
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
        return new FlagPack();
    }

    @Override
    public ItemStack getItemStack() {
        Logger.log(Level.WARNING, "Impossible getItemStack from: "+getConfig().getName());
        return null;
    }

}
