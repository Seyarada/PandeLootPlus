package net.seyarada.pandeloot.nms;

import org.bukkit.Bukkit;

public class NMSHelper {

    static Boolean isOlderThanPlayerTrackEntityEventCache;
    public static boolean isOlderThanPlayerTrackEntityEvent() {
        if(isOlderThanPlayerTrackEntityEventCache!=null) return isOlderThanPlayerTrackEntityEventCache;

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1)
                .substring(3)
                .replace("_R", ".");
        double numericVersion = Double.parseDouble(version);

        isOlderThanPlayerTrackEntityEventCache = numericVersion <= 19;
        return isOlderThanPlayerTrackEntityEventCache;
    }

}
