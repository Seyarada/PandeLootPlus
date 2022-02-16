package net.seyarada.pandeloot.config;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.PredefinedDropsManager;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static boolean debug;
    public static boolean ignoreCitizensDamage;

    public static final List<YamlConfiguration> containers = new ArrayList<>();
    public static final List<YamlConfiguration> drops = new ArrayList<>();
    public static final List<YamlConfiguration> mobs = new ArrayList<>();
    static final File containerFolder = new File(Constants.DATA_FOLDER, "containers");
    static final File dropFolder = new File(Constants.DATA_FOLDER, "drops");
    static final File mobsFolder = new File(Constants.DATA_FOLDER, "mobs");

    static YamlConfiguration configFile;
    static YamlConfiguration eventsFile;

    static File boosterFile;
    static File pityFile;
    static YamlConfiguration boosterConfig;
    static YamlConfiguration pityConfig;



    public static List<Storable> storables = new ArrayList<>();

    public Config() {
        storables.add(new ContainerManager());
        storables.add(new Boosts());
        storables.add(new Pity());
        storables.add(new PredefinedDropsManager());
        reload();
    }

    public static void reload() {
        containers.clear();
        drops.clear();
        mobs.clear();
        generateFile("containers", "loottable");
        generateFile("drops", "exampledrops");
        generateFile("mobs", "examplemobs");
        configFile = YamlConfiguration.loadConfiguration(generateFile("", "config"));
        eventsFile = YamlConfiguration.loadConfiguration(generateFile("", "events"));
        boosterFile = generateFile("", "boosters");
        pityFile = generateFile("", "pity");
        boosterConfig = YamlConfiguration.loadConfiguration(boosterFile);
        pityConfig = YamlConfiguration.loadConfiguration(pityFile);
        loadPredefinedFlags(YamlConfiguration.loadConfiguration(generateFile("", "flags")));
        loadContainersToList();

        storables.forEach(Storable::load);

        debug = configFile.getBoolean("Settings.Debug");
        ignoreCitizensDamage = configFile.getBoolean("Settings.IgnoreCitizensDamage");
    }

    static void loadContainersToList() {
        for(File file : containerFolder.listFiles()) {
            containers.add(YamlConfiguration.loadConfiguration(file));
        }
        for(File file : dropFolder.listFiles()) {
            drops.add(YamlConfiguration.loadConfiguration(file));
        }
        for(File file : mobsFolder.listFiles()) {
            mobs.add(YamlConfiguration.loadConfiguration(file));
        }
    }

    static void loadPredefinedFlags(YamlConfiguration fullConfig) {
        int i = 0;
        for(String key : fullConfig.getKeys(false)) {
            ConfigurationSection section = fullConfig.getConfigurationSection(key);
            if(section==null) continue;
            FlagPack.fromExtended(section);
            i++;
        }
        Logger.log("Loaded %o flag packs", i);
    }

    public static File generateFile(String internalPath, String fileName) {
        if(!internalPath.isEmpty()) new File(Constants.DATA_FOLDER, internalPath).mkdirs();
        fileName = internalPath+"/"+fileName+".yml";

        File file = new File(Constants.DATA_FOLDER, fileName);
        if (!file.exists()) {
            try { // Load default
                InputStreamReader iSR = new InputStreamReader(PandeLoot.inst.getResource(file.getName()));
                FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(iSR);
                internalConfig.options().copyDefaults(true);
                internalConfig.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static ConfigurationSection getMob(Entity entity) {

        final String entityType = entity.getType().toString();
        final String display = entity.getCustomName();
        final String world = entity.getWorld().getName();

        for(YamlConfiguration mobFile : mobs) {
            for(String i : mobFile.getKeys(false)) {

                ConfigurationSection subConfig = mobFile.getConfigurationSection(i);
                final String subType = subConfig.getString("Type");
                if(subType!=null && !subType.equalsIgnoreCase(entityType)) continue;

                final String subDisplay = subConfig.getString("Display");
                if(subDisplay!=null && !subDisplay.equalsIgnoreCase(display)) continue;

                final String subWorld = subConfig.getString("World");
                if(subWorld!=null && !subWorld.equalsIgnoreCase(world)) continue;

                return subConfig;
            }
        }
        return null;
    }

    public static List<String> getScoreHologram() {
        return configFile.getStringList("Announcements.ScoreHologram");
    }
    public static List<String> getScoreMessage() {
        return configFile.getStringList("Announcements.ScoreMessage");
    }

    public static boolean blockBreak() {
        return eventsFile.getBoolean("onBlockBreak.Enabled");
    }
    public static String blockBreakFlags() {
        return eventsFile.getString("onBlockBreak.Flags");
    }
    public static boolean playerDrop() {
        return eventsFile.getBoolean("onPlayerItemDrop.Enabled");
    }
    public static String playerDropFlags() {
        return eventsFile.getString("onPlayerItemDrop.Flags");
    }

}
