package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.utils.ChatUtils;
import net.seyarada.pandeloot.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!sender.isOp()) return true;

        if(args.length>0) {
             switch(args[0]) {
                 case "boost" ->
                         BoosterCommand.onCommand(sender, command, s, args);
                 case "give" ->
                         GiveCommand.onCommand(sender, command, s, args);
                 case "drop" ->
                         DropCommand.onCommand(sender, command, s, args);
                 case "reload" ->
                         ReloadCommand.onCommand(sender, command, s, args);
                 case "gui" ->
                         MenuCommand.onCommand(sender, command, s, args);
            }
        } else {
            ChatUtils.sendCenteredMessage(sender, Constants.DECORATED_NAME);
            String i = ChatColor.YELLOW + "/ploot " + ChatColor.WHITE;
            String div = " - " + ChatColor.GRAY;
            String[] msgList = new String[] {
                    "",
                    i + "boost" + div + "Manage duration and power of loot boost",
                    i + "give" + div + "Give an item to the inventory of a player",
                    i + "drop" + div + "Drop an item to a player",
                    i + "reload" + div + "Reloads the plugin",
                    i + "gui" + div + "Opens a GUI where you can test loot tables",
                    ""
            };
            sender.sendMessage(msgList);
            ChatUtils.sendCenteredMessage(sender, Constants.DECORATED_NAME);
        }
        return true;
    }

    public CommandManager() {
        GiveCommand.loadCompletions();
        DropCommand.loadCompletions();
        MenuCommand.loadCompletions();
        ReloadCommand.loadCompletions();
        BoosterCommand.loadCompletions();
    }

}
