package net.seyarada.pandeloot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.PandeLoot;

public class StringUtils {

    public static String parse(String text) {
        if(text==null) return null;

        try {
            return ChatUtils.translateHexCodes(text);
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static double parseAndMath(String text) {
        if(text==null) return 0;

        if(PandeLoot.papiEnabled) {
            text = PlaceholderAPI.setPlaceholders(null, text);
        }

        try {
            return MathUtils.eval(text);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
