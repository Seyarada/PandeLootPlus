package net.seyarada.pandeloot.drops.containers;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.config.Storable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class ContainerManager implements Storable {

    static final HashMap<String, IContainer> containersGeneric = new HashMap<>();
    static final HashMap<String, LootBag> lootBags = new HashMap<>();

    static int loadedContainers = 0;

    @Override
    public void load() {
        loadedContainers = 0;
        containersGeneric.clear();
        lootBags.clear();

        for(YamlConfiguration config : Config.containers) {
            for(String key : config.getKeys(false)) {
                if(containersGeneric.containsKey(key)) {
                    Logger.userWarning("Duplicate container key: %s", key);
                    continue;
                }
                ConfigurationSection configSection = config.getConfigurationSection(key);
                loadContainer(configSection, key);
            }
        }
        containersGeneric.values().forEach(IContainer::load);
        Logger.userInfo("Loaded %o containers", loadedContainers);
    }

    static void loadContainer(ConfigurationSection configSection, String id) {
        LootTable lootTable = new LootTable(configSection);
        containersGeneric.put(id, lootTable);

        if(configSection.contains("Material"))
            lootBags.put(id, new LootBag(configSection));

        loadedContainers++;
    }

    @Override
    public void save() {}

    public static List<String> getLootTables() {
        return containersGeneric.keySet().stream().toList();
    }

    public static List<String> getLootBags() {
        return lootBags.keySet().stream().toList();
    }

    public static IContainer get(String str) {
        return containersGeneric.get(str);
    }

    public static IContainer get(String str, ContainerType type) {
        return switch (type) {
            case GENERIC -> containersGeneric.get(str);
            case LOOTBAG -> lootBags.get(str);
        };
    }


}
