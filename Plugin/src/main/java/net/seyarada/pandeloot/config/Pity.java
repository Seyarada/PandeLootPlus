package net.seyarada.pandeloot.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;

public class Pity implements Storable {

    public static HashMap<String, HashMap<String, Double>> pity = new HashMap<>();

    public static double getPity(Player player, String id) {
        if(pity.containsKey(player.getUniqueId().toString()))
            return pity.get(player.getUniqueId().toString()).getOrDefault(id, 1d);
        return 1;
    }

    @Override
    public void load() {
        pity.clear();
        Config.pityConfig.getKeys(false).forEach(s -> {
            HashMap<String, Double> innerMap = new HashMap<>();
            ConfigurationSection section = Config.pityConfig.getConfigurationSection(s);
            section.getKeys(false).forEach(k -> innerMap.put(k, section.getDouble(k)));
            pity.put(s, innerMap);
        });

    }

    @Override
    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        pity.forEach((s, innerMap) -> innerMap.forEach((k, p) -> config.set(s + "." + k, p)));
        try {
            config.save(Config.pityFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}