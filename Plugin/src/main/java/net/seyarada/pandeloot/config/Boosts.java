package net.seyarada.pandeloot.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Boosts implements Storable {

    public static HashMap<String, BoostData> boosts = new HashMap<>();

    public static void registerBoost(String id, long duration, double boost) {
        boosts.put(id, new BoostData(System.currentTimeMillis(), duration, boost));
    }

    public static void clearExpired() {
        List<String> toDelete = new ArrayList<>();
        boosts.forEach((id, bData) -> {
            long timeLeft = bData.startTime()+bData.duration()-System.currentTimeMillis();
            if(timeLeft<0) toDelete.add(id);
        } );
        toDelete.forEach(id -> boosts.remove(id));
    }

    public static double getBoost(String id) {
        if(boosts.containsKey(id)) {
            BoostData bData = boosts.get(id);
            long timeLeft = bData.startTime()+bData.duration()-System.currentTimeMillis();
            if(timeLeft>0) return bData.boost();
        }
        return 1;
    }

    @Override
    public void load() {
        boosts.clear();
        Config.boosterConfig.getKeys(false).forEach(s -> {
            long startTime = Config.boosterConfig.getLong(s + ".StartTime");
            long duration = Config.boosterConfig.getLong(s + ".Duration");
            double boost = Config.boosterConfig.getDouble(s + ".Boost");
            BoostData bData = new BoostData(startTime, duration, boost);
            boosts.put(s, bData);
        });
    }

    @Override
    public void save() {
        clearExpired();
        YamlConfiguration config = new YamlConfiguration();
        boosts.forEach((s, bData) -> {
            config.set(s + ".StartTime", bData.startTime());
            config.set(s + ".Duration", bData.duration());
            config.set(s + ".Boost", bData.boost());
        });
        try {
            config.save(Config.boosterFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}