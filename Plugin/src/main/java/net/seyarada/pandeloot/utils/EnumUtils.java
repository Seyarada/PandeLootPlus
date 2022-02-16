package net.seyarada.pandeloot.utils;


import net.seyarada.pandeloot.flags.enums.FlagTrigger;

public class EnumUtils {

    public static boolean isATrigger(String id) {
        for (FlagTrigger trigger : FlagTrigger.values()) {
            if(trigger.name().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

}
