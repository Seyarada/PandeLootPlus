package net.seyarada.pandeloot.commands;

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
