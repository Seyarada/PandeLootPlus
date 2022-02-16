package net.seyarada.pandeloot;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;

import java.io.File;

public class Constants {

    public static final net.md_5.bungee.api.ChatColor ACCENT = net.md_5.bungee.api.ChatColor.of("#d0bb94");
    public static final String PLUGIN_NAME = "PandeLoot";
    public static final String DECORATED_NAME = ACCENT +"["+ PLUGIN_NAME +"] "+ ChatColor.RESET;

    public static final NamespacedKey KEY = new NamespacedKey(PandeLoot.inst, PLUGIN_NAME);
    public static final NamespacedKey LOOTBAG_KEY = new NamespacedKey(PandeLoot.inst, "lootbag");
    public static final NamespacedKey LOCK_LOOTBAG = new NamespacedKey(PandeLoot.inst, "lock_lootbag");
    public static final NamespacedKey UNSTACKABLE_KEY = new NamespacedKey(PandeLoot.inst, "unstackable");

    public static final File DATA_FOLDER = PandeLoot.inst.getDataFolder();

    public static final String MM = "MYTHIC_TYPE";
}
