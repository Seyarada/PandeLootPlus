package net.seyarada.pandeloot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.DropMeta;

public class StringParser {

    public static String parse(String text, DropMeta meta) {
        if(text==null) return null;

        try {
            text = ChatUtils.translateHexCodes(text);

            if(meta!=null) {
                if(meta.lootDrop()!=null) text = meta.lootDrop().parse(text);
            }

            return text;
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
