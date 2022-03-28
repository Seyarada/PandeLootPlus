package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(args.length<2) {
            ChatUtils.sendCenteredMessage(sender, Constants.DECORATED_NAME);
            String i = ChatColor.YELLOW + "/ploot drop " + ChatColor.WHITE;
            String div = ChatColor.WHITE + " - " + ChatColor.GRAY;
            String exmpl = ChatColor.YELLOW+"[Example]"+div;
            String[] msgList = new String[] {
                    "",
                    i + "[Player] [Drop Line]" + div + "Drops an item to the player",
                    exmpl + "drop Player diamond",
                    exmpl + "drop Player mm:mythicSword",
                    exmpl + "drop Player mm:Spear{color=GREEN;explode=false}",
                    ""
            };
            sender.sendMessage(msgList);
            ChatUtils.sendCenteredMessage(sender, Constants.DECORATED_NAME);
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player==null) {
            String i = Constants.DECORATED_NAME + ChatColor.YELLOW;
            sender.sendMessage(i + "Couldn't find a valid player");
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
