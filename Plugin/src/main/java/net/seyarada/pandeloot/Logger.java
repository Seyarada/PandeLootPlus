package net.seyarada.pandeloot;

import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.utils.ColorUtils;
import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.logging.Level;

public class Logger {

    static java.util.logging.Logger bLogger = Bukkit.getLogger();

    static boolean isRecording = false;
    static LinkedList<String> recordedLogs = new LinkedList<>();

    public static void log(String msg) {
        if(isRecording) recordedLogs.add(msg);
        else quickLog(Level.INFO, msg);
    }

    public static void log(String str, Object... args) {
        String msg = String.format(str, args);
        if(isRecording) recordedLogs.add(msg);
        else quickLog(Level.INFO, msg);
    }

    public static void log(Object obj) {
        String msg = obj.toString();
        if(isRecording) recordedLogs.add(msg);
        else quickLog(Level.INFO, msg);
    }

    public static void log(Level level, String msg) {
        if(isRecording) recordedLogs.add(msg);
        else quickLog(level, msg);
    }


    public static void log(Level level, String str, Object... args) {
        String msg = String.format(str, args);
        if(isRecording) recordedLogs.add(msg);
        else quickLog(level, msg);
    }

    public static void log(Level level, Object obj) {
        String msg = obj.toString();
        if(isRecording) recordedLogs.add(msg);
        else quickLog(level, msg);
    }

    public static void record() {
        recordedLogs.clear();
        isRecording = true;
    }

    public static void print() {
        isRecording = false;
        if(Config.DEBUG) {
            if(recordedLogs.size()==0) return;
            float degrees = 0;
            for(String msg : recordedLogs) {
                String c = ColorUtils.hsbToAnsi(degrees, 0.5f, 1);
                bLogger.log(Level.INFO, Constants.CONSOLE_DECORATED_NAME + c + msg);
                degrees += 0.05;
            }
            recordedLogs.clear();
        }
    }

    public static void quickLog(Level level, String log) {
        if(Config.DEBUG) bLogger.log(level, Constants.CONSOLE_DECORATED_NAME + log);
    }

    public static void userInfo(String msg) {
        bLogger.log(Level.INFO, Constants.CONSOLE_DECORATED_NAME + msg);
    }

    public static void userInfo(String str, Object... args) {
        String msg = String.format(str, args);
         bLogger.log(Level.INFO, Constants.CONSOLE_DECORATED_NAME + msg);
    }

    public static void userWarning(String msg) {
        bLogger.log(Level.WARNING, Constants.CONSOLE_DECORATED_NAME + msg);
    }

    public static void userWarning(String str, Object... args) {
        String msg = String.format(str, args);
        bLogger.log(Level.WARNING, Constants.CONSOLE_DECORATED_NAME + msg);
    }

}
