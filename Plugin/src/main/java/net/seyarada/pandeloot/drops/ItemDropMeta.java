package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.StringParser;

public record ItemDropMeta(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop iDrop, FlagTrigger trigger) {

    public String getOrDefault(String key, String defaultValue) {
        return StringParser.parse(values.getOrDefault(key, defaultValue), this);
    }

    public String getString() {
        return StringParser.parse(values.getString("value"), this);
    }

    public String getString(String key) {
        return StringParser.parse(values.getString(key), this);
    }

    public int getInt() {
        return (int) StringParser.parseAndMath(values.getString("value"), lootDrop);
    }

    public int getInt(String key) {
        return (int) StringParser.parseAndMath(values.getString(key), lootDrop);
    }

    public int getIntOrDefault(String key, int defaultInt) {
        return (int) StringParser.parseAndMath(values.getOrDefault(key, String.valueOf(defaultInt)), lootDrop);
    }

    public double getDouble() {
        return StringParser.parseAndMath(values.getString("value"), lootDrop);
    }

    public double getDouble(String key) {
        return StringParser.parseAndMath(values.getString(key), lootDrop);
    }

    public double getDoubleOrDefault(String key, double defaultDouble) {
        return StringParser.parseAndMath(values.getOrDefault(key, String.valueOf(defaultDouble)), lootDrop);
    }

    public long getLong() {
        return (long) StringParser.parseAndMath(values.getString("value"), lootDrop);
    }

    public long getLong(String key) {
        return (long) StringParser.parseAndMath(values.getString(key), lootDrop);
    }

    public long getLongOrDefault(String key, long defaultLong) {
        return (long) StringParser.parseAndMath(values.getOrDefault(key, String.valueOf(defaultLong)), lootDrop);
    }

    public boolean getBoolean() {
        return values.getBoolean("value");
    }

    public boolean getBoolean(String key) {
        return values.getBoolean(key);
    }

    public boolean getBooleanOrDefault(String key, boolean defaultBoolean) {
        return values.getBooleanOrDefault(key, defaultBoolean);
    }

}