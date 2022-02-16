package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.config.Boosts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BoosterCommand {

    public static void loadCompletions() {
        AutoCompletion.add(1, "boost");
        AutoCompletion.add(2, "boost.list");
        AutoCompletion.add(2, "boost.set");
        AutoCompletion.add(2, "boost.stop");
        AutoCompletion.add(3, "boost.stop.players*");
        AutoCompletion.add(3, "boost.stop.Leave this empty for global boost");
        AutoCompletion.add(3, "boost.set.time in seconds");
        AutoCompletion.add(4, "boost.set.*.boost power");
        AutoCompletion.add(5, "boost.set.*.*.players*");
        AutoCompletion.add(5, "boost.set.*.*.Leave this empty for global boost");
    }

    public static void onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[1]) {
            case "set" -> {
                if (args.length == 5) { // Player Boost
                    Boosts.registerBoost(Bukkit.getPlayer(args[4]).getUniqueId().toString(), Long.parseLong(args[2])*1000, Double.parseDouble(args[3]));
                    String text = "Boost of &bx" + args[3] + "&a power set for &b" + args[2] + " seconds &afor &b" + args[4];
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
                    return;
                }
                // Global Boost
                Boosts.registerBoost("Global", Long.parseLong(args[2]), Double.parseDouble(args[3]));
                String text = "Global Boost of &bx" + args[3] + "&a power set for &b" + args[2] + " seconds";
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
            }
            case "list" -> {
                Boosts.clearExpired();
                sender.sendMessage("List of active boosts:");
                Boosts.boosts.forEach((id, bData) -> {
                    int timeLeft = (int) ((bData.startTime()+bData.duration()-System.currentTimeMillis() ) / 1000);
                    sender.sendMessage(id+": "+bData.boost()+"x power, "+timeLeft+" seconds remaining");
                } );
            }
            case "stop" -> {
                if (args.length == 2) {
                    Boosts.boosts.remove("Global");
                    sender.sendMessage("Terminated global boost");
                }
                if (args.length == 3) {
                    Boosts.boosts.remove(Bukkit.getPlayer(args[2]).getUniqueId().toString());
                    sender.sendMessage("Terminated " + args[2] + "'s boost");
                }
            }
        }
    }

}
