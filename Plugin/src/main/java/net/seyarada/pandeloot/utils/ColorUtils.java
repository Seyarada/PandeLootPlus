package net.seyarada.pandeloot.utils;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class ColorUtils {

    public static Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
    static final HashMap<ChatColor, org.bukkit.ChatColor> conversionCache = new HashMap<>();

    public static org.bukkit.ChatColor fromBungeeToBukkit(ChatColor color) {
        if(conversionCache.containsKey(color))
            return conversionCache.get(color);

        org.bukkit.ChatColor selectedColor = org.bukkit.ChatColor.WHITE;
        double colorDistance = 9999;

        for(org.bukkit.ChatColor cC : org.bukkit.ChatColor.values()) {
            if(cC.asBungee().getColor()==null) continue;
            double dist = colorDistance(color.getColor(), cC.asBungee().getColor());
            if(dist<colorDistance) {
                colorDistance = dist;
                selectedColor = cC;
            }
        }

        conversionCache.put(color, selectedColor);

        return selectedColor;
    }

    static double colorDistance(Color c1, Color c2) {
        int red1 = c1.getRed();
        int red2 = c2.getRed();
        int rMean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        return Math.sqrt((((512+rMean)*r*r)>>8) + 4*g*g + (((767-rMean)*b*b)>>8));
    }

    public static org.bukkit.Color getRGB(String color) {
        switch(color.toUpperCase()) {
            case "YELLOW":       return org.bukkit.Color.fromRGB(255, 255, 85);
            case "LIGHT_PURPLE": return org.bukkit.Color.fromRGB(255, 85, 255);
            case "GREEN":        return org.bukkit.Color.fromRGB(85, 255, 85);
            case "GOLD":         return org.bukkit.Color.fromRGB(255, 170, 0);
            case "DARK_RED":     return org.bukkit.Color.fromRGB(170, 0, 0);
            case "GRAY":         return org.bukkit.Color.fromRGB(128,128,128);
            case "DARK_PURPLE":  return org.bukkit.Color.fromRGB(170, 0, 170);
            case "DARK_GREEN":   return org.bukkit.Color.fromRGB(0, 170, 0);
            case "DARK_GRAY":    return org.bukkit.Color.fromRGB(85, 85, 85);
            case "DARK_BLUE":    return org.bukkit.Color.fromRGB(0, 0, 170);
            case "DARK_AQUA":    return org.bukkit.Color.fromRGB(0, 170, 170);
            case "BLUE":         return org.bukkit.Color.fromRGB(85, 85, 255);
            case "BLACK":        return org.bukkit.Color.fromRGB(0, 0, 0);
            case "AQUA":         return org.bukkit.Color.fromRGB(85, 255, 255);
            case "RED":          return org.bukkit.Color.fromRGB(255, 85, 85);
            default:             return org.bukkit.Color.fromRGB(255, 255, 255);
        }
    }

    public static String getRandomColorString() {
        Random r = new Random();
        int randomHex = r.nextInt(0xffffff + 1);
        return String.format("#%06x", randomHex);
    }

    public static ChatColor getRandomColor() {
        Random r = new Random();
        int randomHex = r.nextInt(0xffffff + 1);
        return ChatColor.of(String.format("#%06x", randomHex));
    }

    public static void setItemColor(Entity item, ChatColor color, Player player) {
        final org.bukkit.ChatColor bukkitColor = fromBungeeToBukkit(color);
        final String teamName = bukkitColor + Constants.PLUGIN_NAME;

        Scoreboard toUseBoard = player == null ? board : player.getScoreboard();

        if (toUseBoard.getTeam(teamName) == null) {
            Team team = toUseBoard.registerNewTeam(teamName);
            team.addEntry(item.getUniqueId().toString());
            team.setColor(bukkitColor);
            return;
        }

        toUseBoard.getTeam(teamName).addEntry(item.getUniqueId().toString());
    }

    public static String findStringColor(String color) {
        return switch (color) {
            case "0" -> "BLACK";
            case "1" -> "DARK_BLUE";
            case "2" -> "DARK_GREEN";
            case "3" -> "DARK_AQUA";
            case "4" -> "DARK_RED";
            case "5" -> "DARK_PURPLE";
            case "6" -> "GOLD";
            case "7" -> "GRAY";
            case "8" -> "DARK_GRAY";
            case "9" -> "BLUE";
            case "a" -> "GREEN";
            case "b" -> "AQUA";
            case "c" -> "RED";
            case "d" -> "LIGHT_PURPLE";
            case "e" -> "YELLOW";
            default -> "WHITE";
        };
    }

}
