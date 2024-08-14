package net.seyarada.pandeloot.nms;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class NMSManager {

    public static final String CONSOLE_ACCENT = "\u001b[38;5;180m";
    public static final String CONSOLE_ACCENT_RESET = "\u001b[0m";
    public static final String PLUGIN_NAME = "PandeLoot";
    public static final String DECORATED_NAME = CONSOLE_ACCENT +"["+ PLUGIN_NAME +"] "+ CONSOLE_ACCENT_RESET;

    static final Map<Integer, List<UUID>> hiddenItems = new ConcurrentHashMap<>();
    static NMSMethods nms;

    static {
        try {
            String version = getNMSVersion();

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

    public static String getNMSVersion() {
        String mcVersion = Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf('-'));
        return switch (mcVersion){ // blame paper
            case "1.17.1" -> "v1_17_R1";
            case "1.18", "1.18.1" -> "v1_18_R1";
            case "1.18.2" -> "v1_18_R2";
            case "1.19", "1.19.1", "1.19.2" -> "v1_19_R1";
            case "1.19.3" -> "v1_19_R2";
            case "1.19.4" -> "v1_19_R3";
            case "1.20", "1.20.1" -> "v1_20_R1";
            case "1.20.2" -> "v1_20_R2";
            case "1.20.3", "1.20.4" -> "v1_20_R3";
            case "1.21", "1.21.1" -> "v1_21_R1";

            default -> throw new RuntimeException("Unknown NMS version for MC " + mcVersion);
        };
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
