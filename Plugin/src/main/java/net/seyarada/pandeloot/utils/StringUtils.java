package net.seyarada.pandeloot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.PandeLoot;

public class StringUtils {

    public static String parse(String text) {
        return ChatUtils.translateHexCodes(text);
    }

    public static double parseAndMath(String text) {
        if(PandeLoot.papiEnabled) {
            text = PlaceholderAPI.setPlaceholders(null, text);
        }

        return MathUtils.eval(text);
    }

}
