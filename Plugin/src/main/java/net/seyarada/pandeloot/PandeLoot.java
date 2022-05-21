package net.seyarada.pandeloot;

import net.seyarada.pandeloot.commands.AutoCompletion;
import net.seyarada.pandeloot.commands.CommandManager;
import net.seyarada.pandeloot.compatibility.VaultCompatibility;
import net.seyarada.pandeloot.compatibility.citizens.CitizensCompatibility;
import net.seyarada.pandeloot.compatibility.mmocore.MMOCoreCompatibility;
import net.seyarada.pandeloot.compatibility.mythicmobs.MythicMobsListener;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.config.Storable;
import net.seyarada.pandeloot.drops.ActiveDropListener;
import net.seyarada.pandeloot.drops.DropEvents;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.gui.ContainersGUI;
import net.seyarada.pandeloot.loot.ItemProviderManager;
import net.seyarada.pandeloot.loot.LootProviderManager;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.nms.PlayerPacketListener;
import net.seyarada.pandeloot.trackers.DamageTracker;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PandeLoot extends JavaPlugin implements Listener {

    public static PandeLoot inst;
    public static boolean papiEnabled = false;
    public static boolean mythicEnabled = false;
    public static boolean ecoEnabled = false;
    public static boolean discordEnabled = false;
    public static boolean mmoItemsEnabled = false;

    @Override
    public void onEnable() {
        inst = this;
        checkCompatibilities();


        ItemProviderManager.init();
        LootProviderManager.init();

        new FlagManager();
        Config.load();
        new NMSManager();

        PluginManager pluginManager = getServer().getPluginManager();

        getCommand("pandeloot").setExecutor(new CommandManager());
        getCommand("pandeloot").setTabCompleter(new AutoCompletion());

        getCommand("ploot").setExecutor(new CommandManager());
        getCommand("ploot").setTabCompleter(new AutoCompletion());

        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new ActiveDropListener(), this);
        pluginManager.registerEvents(new DamageTracker(), this);
        pluginManager.registerEvents(new PlayerPacketListener(), this);
        pluginManager.registerEvents(new DropEvents(), this);
        pluginManager.registerEvents(new ContainersGUI(null), this);
    }

    @Override
    public void onDisable() {
        Config.storables.forEach(Storable::save);
    }

    void checkCompatibilities() {
        PluginManager pluginManager = getServer().getPluginManager();
        if(pluginManager.getPlugin("MythicMobs")!=null) {
            mythicEnabled = true;
            pluginManager.registerEvents(new MythicMobsListener(), this);
            Logger.userInfo("Loaded MythicMobs support");
        }

        if(pluginManager.getPlugin("Vault")!=null) {
            ecoEnabled = true;
            VaultCompatibility.setupEconomy();
            Logger.userInfo("Loaded Vault support");
        }

        if(pluginManager.getPlugin("Citizens")!=null) {
            CitizensCompatibility.enabled = true;
            Logger.userInfo("Loaded Citizens support");
        }

        if(pluginManager.getPlugin("PlaceholderAPI")!=null) {
            papiEnabled = true;
            Logger.userInfo("Loaded PAPI support");
        }

        if(pluginManager.getPlugin("DiscordSRV")!=null) {
            discordEnabled = true;
            Logger.userInfo("Loaded DiscordSRV support");
        }

        if(pluginManager.getPlugin("MMOItems")!=null) {
            mmoItemsEnabled = true;
            Logger.userInfo("Loaded MMOItems support");
        }

        if(pluginManager.getPlugin("MMOCore")!=null) {
            pluginManager.registerEvents(new MMOCoreCompatibility(), this);
            Logger.userInfo("Loaded MMOCore support");
        }
    }

}
