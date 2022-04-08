package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.StringParser;

public record DropMeta(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop iDrop, FlagTrigger trigger) {

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
        return values.getInt("value");
    }

    public int getInt(String key) {
        return  values.getInt(key);
    }

    public int getIntOrDefault(String key, int defaultInt) {
        return values.getIntOrDefault(key, defaultInt);
    }

    public double getDouble() {
        return values.getDouble("value");
    }

    public double getDouble(String key) {
        return values.getDouble(key);
    }

    public double getDoubleOrDefault(String key, double defaultDouble) {
        return values.getDoubleOrDefault(key, defaultDouble);
    }

    public long getLong() {
        return values.getLong("value");
    }

    public long getLong(String key) {
        return values.getLong(key);
    }

    public long getLongOrDefault(String key, long defaultLong) {
        return values.getLongOrDefault(key, defaultLong);
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