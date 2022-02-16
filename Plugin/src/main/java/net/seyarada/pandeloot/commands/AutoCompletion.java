package net.seyarada.pandeloot.commands;

import net.seyarada.pandeloot.drops.containers.ContainerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class AutoCompletion implements TabCompleter {

    static final List<String> COMMANDS = Arrays.asList("reload", "drop", "give", "boost", "pity");
    static final List<String> BLANK = Arrays.asList("", "");

    public static Map<Integer, List<String>> autoComplete = new HashMap<>();
    public static void add(int position, String string) {
        List<String> list = autoComplete.getOrDefault(position, new ArrayList<>());
        list.add(string);
        autoComplete.put(position, list);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(!autoComplete.containsKey(args.length)) return BLANK;

        List<String> toShow = new ArrayList<>();

        a: for(String entry : autoComplete.get(args.length)) {
            String[] entries = entry.split("\\.");
            if(entries.length<args.length) continue; // Ignore completion path if arg numbers don't match
            for (int i = 0; i < args.length-1; i++) {
                if(!entries[i].contains("*")) {
                    if (!entries[i].startsWith(args[i]))
                        continue a;
                }
            }

            String completion = entries[args.length-1];

            switch (completion.toLowerCase().replace("*", "")) {
                case "players" -> {
                    Bukkit.getOnlinePlayers().forEach(p -> toShow.add(p.getName()));
                }
                case "items" -> {
                    ContainerManager.getLootBags().forEach(s -> toShow.add("lb:"+s));
                }
                case "containers" -> {
                    ContainerManager.getLootTables().forEach(s -> toShow.add("lt:"+s));
                    ContainerManager.getLootBags().forEach(s -> toShow.add("lb:"+s));
                }
                case "*" -> {
                }
                default -> toShow.add(completion);
            }


        }

        return toShow;
    }

}
