package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

    public static void loadCompletions() {
        AutoCompletion.add(1, "reload");
    }

    public static void onCommand(CommandSender sender, Command command, String label, String[] args) {
        Config.reload();
        Logger.log("Plugin Reloaded!");
        if(sender instanceof Player player) {
            player.sendMessage(ChatColor.YELLOW+"Reloaded!");
        }
    }

}
