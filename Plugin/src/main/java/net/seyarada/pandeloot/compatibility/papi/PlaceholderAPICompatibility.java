package net.seyarada.pandeloot.compatibility.papi;

import org.bukkit.entity.Player;

public class PlaceholderAPICompatibility {

    public static String parse(String text, Player player) {
        return  me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, text);
    }

}
