package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.gui.ContainersGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand {

    public static void loadCompletions() {
        AutoCompletion.add(1, "gui");
    }

    public static void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            new ContainersGUI(player);
        }
    }

}
