package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.drops.IDrop;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand {

    public static void loadCompletions() {
        AutoCompletion.add(1, "give");
        AutoCompletion.add(2, "give.players*");
        AutoCompletion.add(3, "give.players*.items*");
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

        ItemStack iS = IDrop.getAsDrop(sb.toString(), player).getItemStack();
        if(iS==null) {
            player.sendMessage("Couldn't find an item for that drop");
            return;
        }

        player.getInventory().addItem(iS);
    }

}
