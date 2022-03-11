package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class DropCommand {

    public static void loadCompletions() {
        AutoCompletion.add(1, "drop");
        AutoCompletion.add(2, "drop.players*");
        AutoCompletion.add(3, "drop.players*.containers*");
    }

    public static void onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = Bukkit.getPlayer(args[1]);
        if(player==null) {
            sender.sendMessage("Couldn't find a valid player");
            return;
        }

        // Gets everything after arg[1]
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < args.length-2; i++) {
            if (i > 0) sb.append(" ");
            sb.append(args[2+i]);
        }

        Logger.record();
        IDrop iDrop = IDrop.getAsDrop(sb.toString(), player);
        new LootDrop(Collections.singletonList(iDrop), player, player.getLocation())
                .build()
                .drop();
        Logger.print();
    }

}
