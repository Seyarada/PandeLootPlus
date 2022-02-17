package net.seyarada.pandeloot.nms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class NMSManager {

    public static final net.md_5.bungee.api.ChatColor ACCENT = net.md_5.bungee.api.ChatColor.of("#d0bb94");
    public static final String PLUGIN_NAME = "PandeLoot";
    public static final String DECORATED_NAME = ACCENT +"["+ PLUGIN_NAME +"] "+ ChatColor.RESET;

    static final Map<Integer, List<UUID>> hiddenItems = new ConcurrentHashMap<>();
    static NMSMethods nms;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.version
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        // Get the last element of the package

        try {
            final Class<?> clazz = Class.forName("net.seyarada.pandeloot.nms." + version + "." + version.toUpperCase());
            // Check if we have a NMSHandler class at that location.
            if (NMSMethods.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                nms = (NMSMethods) clazz.getConstructor().newInstance(); // Set our handler
                Bukkit.getLogger().info(DECORATED_NAME + "Loading support for " + version);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe(DECORATED_NAME + "Could not find support for this CraftBukkit version");
        }
    }


    public static void addHiddenItem(int id, List<UUID> players) {
        hiddenItems.put(id, players);
    }

    public static void removeHiddenItem(int id) {
        hiddenItems.remove(id);
    }

    public static boolean isHiddenFor(int id, UUID p) {
        if(hiddenItems.containsKey(id)) {
            return !hiddenItems.get(id).contains(p);
        }
        return false;
    }

    public static NMSMethods get() {
        return nms;
    }

}
