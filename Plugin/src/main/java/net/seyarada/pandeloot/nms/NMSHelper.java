package net.seyarada.pandeloot.nms;

public class NMSHelper {

    static Boolean isOlderThanPlayerTrackEntityEventCache;
    public static boolean isOlderThanPlayerTrackEntityEvent() {
        if(isOlderThanPlayerTrackEntityEventCache!=null) return isOlderThanPlayerTrackEntityEventCache;

        String version = NMSManager.getNMSVersion()
                .substring(3)
                .replace("_R", ".");
        double numericVersion = Double.parseDouble(version);

        isOlderThanPlayerTrackEntityEventCache = numericVersion <= 19;
        return isOlderThanPlayerTrackEntityEventCache;
    }

}
