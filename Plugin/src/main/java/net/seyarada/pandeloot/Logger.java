package net.seyarada.pandeloot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.LinkedList;
import java.util.logging.Level;

public class Logger {

    static java.util.logging.Logger bLogger = Bukkit.getLogger();

    static boolean isRecording = false;
    static LinkedList<String> recordedLogs = new LinkedList<>();

    public static void log(String msg) {
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(Level.INFO, Constants.DECORATED_NAME +msg);
    }


    public static void log(String str, Object... args) {
        String msg = String.format(str, args);
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(Level.INFO, Constants.DECORATED_NAME +msg);
    }

    public static void log(Object obj) {
        String msg = obj.toString();
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(Level.INFO, Constants.DECORATED_NAME +msg);
    }

    public static void log(Level level, String msg) {
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(level, Constants.DECORATED_NAME +msg);
    }


    public static void log(Level level, String str, Object... args) {
        String msg = String.format(str, args);
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(level, Constants.DECORATED_NAME +msg);
    }

    public static void log(Level level, Object obj) {
        String msg = obj.toString();
        if(isRecording) recordedLogs.add(msg);
        else    bLogger.log(level, Constants.DECORATED_NAME +msg);
    }

    public static void record() {
        isRecording = true;
    }

    public static void print() {
        isRecording = false;
        if(recordedLogs.size()==0) return;
        float degrees = 0;
        for(String msg : recordedLogs) {
            ChatColor c = ChatColor.of(Color.getHSBColor(degrees, 0.5f, 1));
            bLogger.log(Level.INFO, Constants.DECORATED_NAME +c+msg);
            degrees += 0.05;
        }
        recordedLogs.clear();
        isRecording = true;
    }

}
