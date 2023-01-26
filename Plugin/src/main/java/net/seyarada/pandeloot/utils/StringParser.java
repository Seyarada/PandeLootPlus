package net.seyarada.pandeloot.utils;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.compatibility.PlaceholderAPICompatibility;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsCompatibility;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.LootDrop;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StringParser {

    static final StrSubstitutor SUBSTITUTOR;

    static {
        final HashMap<String, String> placeholderValues = new HashMap<>();

        placeholderValues.put("rb", "[");
        placeholderValues.put("lb", "]");
        placeholderValues.put("rs", "<");
        placeholderValues.put("ls", ">");
        placeholderValues.put("sc", ";");

        SUBSTITUTOR = new StrSubstitutor(placeholderValues, "%", "%");
    }

    public static String parse(String text, ItemDropMeta meta) {
        if(text==null) return null;

        try {
            return parseText(text, meta.lootDrop());
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static long parseAndMath(String text, LootDrop drop) {
        if(text==null) return 0;

        text = parseText(text, drop);

        try {
            return MathUtils.eval(text);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String parseText(String text, LootDrop drop) {
        text = SUBSTITUTOR.replace(text);
        text = ChatUtils.translateHexCodes(text);

        Player player = (drop==null) ? null : drop.p;

        if(drop!=null)
            text = drop.sub.replace(text);

        if(PandeLoot.mythicEnabled && drop!=null && text.contains("<") && text.contains(">"))
            text = MythicMobsCompatibility.parse(text, drop, player);

        if(player!=null && PandeLoot.papiEnabled)
            text = PlaceholderAPICompatibility.parse(text, player);

        if(text.chars().filter(ch -> ch == '%').count() >= 2)
            return null;

        return text;
    }

}
