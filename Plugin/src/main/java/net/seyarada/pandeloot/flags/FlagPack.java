package net.seyarada.pandeloot.flags;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.enums.FlagPriority;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.*;
import net.seyarada.pandeloot.utils.EnumUtils;
import net.seyarada.pandeloot.utils.StringParser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FlagPack {

    public static HashMap<String, FlagPack> cache = new HashMap<>();
    public static final HashMap<String, FlagPack> predefinedPacks = new HashMap<>();

    public final HashMap<FlagTrigger, HashMap<IFlag, FlagModifiers>> flags = new HashMap<>();
    public final HashMap<FlagTrigger, HashMap<ICondition,  FlagModifiers>> conditionFlags = new HashMap<>();
    public FlagPackFactory.FlagString flagString;

    public FlagPack() {
        merge(Config.defaultFlagPack);
    }

    public FlagPack(boolean ignored) {
        // Allows config.defaultFlagPack to generate without weird shenanigans
    }

    public void trigger(FlagTrigger trigger, Entity entity, LootDrop lootDrop, IDrop iDrop) {
        if(!flags.containsKey(trigger)) return;
        if(lootDrop!=null) lootDrop.dropEntity = entity;

        // TODO: Improve this so it doesn't needs to loop unnecessarily
        for(FlagPriority priority : FlagPriority.values()) {
            for(Map.Entry<IFlag, FlagModifiers> flagClasses : flags.get(trigger).entrySet()) {
                IFlag flagClass = flagClasses.getKey();
                FlagModifiers flagData = flagClasses.getValue();
                if(flagClass.getClass().getAnnotation(FlagEffect.class).priority()!=priority)
                    continue;

                ItemDropMeta meta = new ItemDropMeta(flagData, lootDrop, iDrop, trigger);

                if(flagClass instanceof IGeneralEvent e) {
                    e.onCallGeneral(meta);
                }
                if(flagClass instanceof IItemEvent e && iDrop instanceof ItemDrop d && d.item!=null && lootDrop!=null && lootDrop.dropEntity!=null) {
                    e.onCallItem((Item)entity, meta);
                }
                if(flagClass instanceof IEntityEvent e && entity!=null) {
                    e.onCallEntity(entity, meta);
                }
                if(flagClass instanceof IPlayerEvent e && lootDrop!=null && lootDrop.p!=null) {
                    e.onCallPlayer(lootDrop.p, meta);
                }
            }
        }

    }

    public void trigger(FlagTrigger trigger, Entity entity, Player player) {
        if(!flags.containsKey(trigger)) return;

        // TODO: Improve this so it doesn't needs to loop unnecessarily
        for(FlagPriority priority : FlagPriority.values()) {
            for(Map.Entry<IFlag, FlagModifiers> flagClasses : flags.get(trigger).entrySet()) {
                IFlag flagClass = flagClasses.getKey();
                FlagModifiers flagData = flagClasses.getValue();
                if(flagClass.getClass().getAnnotation(FlagEffect.class).priority()!=priority)
                    continue;

                ItemDropMeta meta = new ItemDropMeta(flagData, null, null, trigger);

                if(flagClass instanceof IGeneralEvent e) {
                    e.onCallGeneral(meta);
                }
                if(flagClass instanceof IItemEvent e) {
                    e.onCallItem((Item)entity, meta);
                }
                if(flagClass instanceof IEntityEvent e && entity!=null) {
                    e.onCallEntity(entity, meta);
                }
                if(flagClass instanceof IPlayerEvent e) {
                    e.onCallPlayer(player, meta);
                }
            }
        }

    }

    public static FlagPack fromCompact(String line) {
        if(cache.containsKey(line)) return cache.get(line);

        FlagPack pack = FlagPackFactory.getPack(line);
        pack.merge(Config.defaultFlagPack);
        cache.put(line, pack);
        Logger.log("Generated flag pack %s from %s", pack, line);
        return pack;
    }

    public static FlagPack fromExtended(ConfigurationSection config) {
        if(predefinedPacks.containsKey(config.getName())) return predefinedPacks.get(config.getName());

        FlagPack pack = new FlagPack();
        pack.configReader(config);
        pack.merge(Config.defaultFlagPack);
        predefinedPacks.put(config.getName(), pack);
        if(Config.debug) Logger.log("Generated flag pack %s from %s", pack, config.getName());
        return pack;
    }

    void configReader(ConfigurationSection config) {
        flags.put(FlagTrigger.onspawn, new HashMap<>());
        for(String s : config.getKeys(false)) {
            if(EnumUtils.isATrigger(s)) {
                FlagTrigger trigger = FlagTrigger.valueOf(s.toLowerCase());
                flags.put(trigger, new HashMap<>());
                ConfigurationSection triggerFlags = config.getConfigurationSection(s);
                for(String f : triggerFlags.getKeys(false)) {
                    IFlag flag = FlagManager.getFromID(f);
                    if(flag==null) continue;
                    FlagModifiers flagValues = processRawDataAndModifiers(triggerFlags.getString(f));
                    if(flag instanceof ICondition condition) {
                        HashMap<ICondition, FlagModifiers> condMap = conditionFlags.getOrDefault(trigger, new HashMap<>());
                        condMap.put(condition, flagValues);
                        conditionFlags.put(trigger, condMap);
                        if( !(flag instanceof IEntityEvent) &&
                                !(flag instanceof IGeneralEvent) &&
                                !(flag instanceof IItemEvent) &&
                                !(flag instanceof IPlayerEvent) &&
                                !(flag instanceof IServerEvent))
                            continue;
                    }
                    flags.get(trigger).put(flag, flagValues);
                }
                continue;
            }
            IFlag flag = FlagManager.getFromID(s);
            if(flag==null) continue;
            FlagModifiers flagValues = processRawDataAndModifiers(config.getString(s));
            if(flag instanceof ICondition condition) {
                HashMap<ICondition, FlagModifiers> condMap = conditionFlags.getOrDefault(FlagTrigger.onspawn, new HashMap<>());
                condMap.put(condition, flagValues);
                conditionFlags.put(FlagTrigger.onspawn, condMap);
                if( !(flag instanceof IEntityEvent) &&
                        !(flag instanceof IGeneralEvent) &&
                        !(flag instanceof IItemEvent) &&
                        !(flag instanceof IPlayerEvent) &&
                        !(flag instanceof IServerEvent))
                    continue;
            }
            flags.get(FlagTrigger.onspawn).put(flag, flagValues);
        }
    }

    void parseRawFlags(HashMap<FlagTrigger, HashMap<String, String>> map) {
        // This takes the raw information from a flag and parses it
        // Input Example: onspawn{give=10,explode=true <shape=sphere>}
        // Output Example: onspawn{give={value=10},explode={value=true,shape=sphere}}

        for(Map.Entry<FlagTrigger, HashMap<String, String>> entry : map.entrySet()) {
            FlagTrigger trigger = entry.getKey();
            HashMap<IFlag, FlagModifiers> triggerMap = new HashMap<>();
            for(Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                String flagName = subEntry.getKey();
                if(flagName==null) continue;
                if(flagName.equals("pack")) {
                    String pack = subEntry.getValue();
                    if(!predefinedPacks.containsKey(pack))
                        Logger.log(Level.WARNING, "Predefined pack %s not found", pack);
                    else
                        merge(predefinedPacks.get(pack));
                    continue;
                }

                IFlag flag = FlagManager.getFromID(flagName);
                if(flag==null) continue;
                FlagModifiers flagValues = processRawDataAndModifiers(subEntry.getValue());
                if(flag instanceof ICondition condition) {
                    HashMap<ICondition, FlagModifiers> condMap = conditionFlags.getOrDefault(trigger, new HashMap<>());
                    condMap.put(condition, flagValues);
                    conditionFlags.put(trigger, condMap);
                    if( !(flag instanceof IEntityEvent) &&
                            !(flag instanceof IGeneralEvent) &&
                            !(flag instanceof IItemEvent) &&
                            !(flag instanceof IPlayerEvent) &&
                            !(flag instanceof IServerEvent))
                        continue;
                }
                triggerMap.put(flag, flagValues);
            }
            flags.put(trigger, triggerMap);
        }
    }

    FlagModifiers processRawDataAndModifiers(String s) {
        FlagModifiers modifiersMap = new FlagModifiers(this);
        if(!s.contains("<")) {
            modifiersMap.put("value", s);
        } else {
            String flagValue = s.substring(0, s.indexOf("<")).trim();
            modifiersMap.put("value", flagValue);

            String modifiersString = s.substring(s.indexOf("<") + 1, s.indexOf(">"));
            for(String modifier : modifiersString.split(";")) {
                String modKey = modifier.substring(0, modifier.indexOf("="));
                String modValue = modifier.substring(modifier.indexOf("=") + 1);
                modifiersMap.put(modKey, modValue.trim());
            }

        }
        return modifiersMap;
    }



    public void writeRawFlagToMap(HashMap<FlagTrigger, HashMap<String, String>> map, FlagTrigger trigger, String flag, String rawData) {
        HashMap<String, String> innerMap = map.getOrDefault(trigger, new HashMap<>());
        innerMap.put(flag, rawData);
        map.put(trigger, innerMap);
    }

    public void merge(FlagPack pack) {
        for(Map.Entry<FlagTrigger, HashMap<IFlag, FlagModifiers>> entry : pack.flags.entrySet()) {
            if(!flags.containsKey(entry.getKey())) {
                flags.put(entry.getKey(), entry.getValue());
            } else {
                HashMap<IFlag, FlagModifiers> subMap = flags.get(entry.getKey());
                for(Map.Entry<IFlag, FlagModifiers> subEntry : entry.getValue().entrySet()) {
                    subMap.putIfAbsent(subEntry.getKey(), subEntry.getValue());
                }
                flags.put(entry.getKey(), subMap);
            }
        }

        for(Map.Entry<FlagTrigger, HashMap<ICondition, FlagModifiers>> entry : pack.conditionFlags.entrySet()) {
            if(!conditionFlags.containsKey(entry.getKey())) {
                conditionFlags.put(entry.getKey(), entry.getValue());
            } else {
                HashMap<ICondition, FlagModifiers> subMap = conditionFlags.get(entry.getKey());
                for(Map.Entry<ICondition, FlagModifiers> subEntry : entry.getValue().entrySet()) {
                    subMap.putIfAbsent(subEntry.getKey(), subEntry.getValue());
                }
                conditionFlags.put(entry.getKey(), subMap);
            }
        }
    }

    public boolean passesConditions(FlagTrigger trigger, Entity entity, Player player) {
        if(!conditionFlags.containsKey(trigger)) return true;
        for(Map.Entry<ICondition, FlagModifiers> entry : conditionFlags.get(trigger).entrySet()) {
            ICondition condition = entry.getKey();
            FlagModifiers values = entry.getValue();
            if(!condition.onCheckNoLootDrop(values, entity, player)) return false;
        }

        return true;
    }


    public static class FlagModifiers extends HashMap<String, String> {

        public final FlagPack pack;

        public FlagModifiers(FlagPack pack) {
            this.pack = pack;
        }

        public String getString() {
            return getString("value");
        }

        public String getString(String key) {
            return get(key);
        }

        public int getInt() {
            return getInt("value");
        }

        public int getInt(String key) {
            if(!containsKey(key)) return 0;
            String value = getString(key);
            return (int) StringParser.parseAndMath(value, null);
        }

        public int getIntOrDefault(String key, int defaultInt) {
            String value = getString(key);
            return (value!=null) ? (int) StringParser.parseAndMath(value, null) : defaultInt;
        }

        public double getDouble() {
            return getDouble("value");
        }

        public double getDouble(String key) {
            if(!containsKey(key)) return 0;
            String value = getString(key);
            return StringParser.parseAndMath(value, null);
        }

        public double getDoubleOrDefault(String key, double defaultDouble) {
            String value = getString(key);
            return (value!=null) ? StringParser.parseAndMath(value, null) : defaultDouble;
        }

        public long getLong() {
            return getLong("value");
        }

        public long getLong(String key) {
            if(!containsKey(key)) return 0;
            String value = getString(key);
            return (long) StringParser.parseAndMath(value, null);
        }

        public long getLongOrDefault(String key, long defaultLong) {
            String value = getString(key);
            return (value!=null) ? (long) StringParser.parseAndMath(value, null) : defaultLong;
        }

        public boolean getBoolean() {
            return getBoolean("value");
        }

        public boolean getBoolean(String key) {
            if(!containsKey(key)) return false;
            String value = getString(key);
            return Boolean.parseBoolean(value);
        }

        public boolean getBooleanOrDefault(String key, boolean defaultBoolean) {
            String value = getString(key);
            return (value!=null) ? Boolean.parseBoolean(value) : defaultBoolean;
        }

    }

    public boolean hasFlag(IFlag flag) {
        if(!flags.containsKey(FlagTrigger.onspawn)) return false;
        return flags.get(FlagTrigger.onspawn).containsKey(flag);
    }

    public boolean hasFlag(Class<? extends IFlag> classFlag) {
        String id = classFlag.getDeclaredAnnotation(FlagEffect.class).id();
        IFlag flag = FlagManager.getFromID(id);
        return flags.get(FlagTrigger.onspawn).containsKey(flag);
    }

    public FlagModifiers getFlag(IFlag flag) {
        return flags.get(FlagTrigger.onspawn).get(flag);
    }

    public FlagModifiers getFlag(Class<? extends IFlag> classFlag) {
        String id = classFlag.getDeclaredAnnotation(FlagEffect.class).id();
        IFlag flag = FlagManager.getFromID(id);
        return flags.get(FlagTrigger.onspawn).get(flag);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        flags.forEach((trigger, value) -> {
            builder.append(trigger.toString()).append("=[");
            value.forEach((flag, flagValue) -> {
                builder.append(FlagManager.getFromClass(flag)).append("=");
                builder.append("<");
                flagValue.forEach((mod, modValue) -> {
                    builder.append(mod).append("=").append(modValue).append(";");
                });
                builder.append(">;");
            });
            if(conditionFlags.containsKey(trigger)) {
                conditionFlags.get(trigger).forEach((condition, condValue) -> {
                    builder.append(FlagManager.getFromClass(condition)).append("=");
                    builder.append("<");
                    condValue.forEach((mod, modValue) -> {
                        builder.append(mod).append("=").append(modValue).append(";");
                    });
                    builder.append(">;");
                });
            }
            builder.append("];");
        });
        String str = "{" + builder + "}";
        str = str.replace(";>", ">").replace(";]", "]").replace(";}", "}");
        return str;
    }

}
