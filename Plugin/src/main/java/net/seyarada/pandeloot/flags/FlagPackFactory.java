package net.seyarada.pandeloot.flags;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.EnumUtils;

import java.util.Arrays;
import java.util.HashMap;

public class FlagPackFactory {

    public static FlagPack getPack(String str) {
        FlagString flagString = new FlagString(str);
        Logger.log("Flag Section: %s", flagString.flagSection);
        Logger.log("Outer Section: %s", flagString.outerSection);
        Logger.log("Origin Section: %s", flagString.origin);
        Logger.log("Item Section: %s", flagString.item);

        HashMap<FlagTrigger, HashMap<String, String>> triggersWithFlags = new HashMap<>();
        FlagPack pack = new FlagPack();
        pack.flagString = flagString;

        HashMap<String, String> outerFlags = new HashMap<>();
        if(flagString.outerSection!=null && !flagString.outerSection.isBlank()) {
            Arrays.stream(flagString.outerSection.split(" ")).forEach(element -> {
                if(element.contains(".")) {
                    outerFlags.put("chance", element);
                } else {
                    outerFlags.put("amount", element);
                }
            });
        }
        triggersWithFlags.put(FlagTrigger.onspawn, outerFlags);

        StringBuilder builder = new StringBuilder();
        FlagTrigger currentTrigger = FlagTrigger.onspawn;
        boolean inModifiers = false;

        if(flagString.flagSection!=null) {
            for (int i = 0; i < flagString.flagSection.length(); i++) {
                char c = flagString.flagSection.charAt(i);

                if(i==flagString.flagSection.length()-1 && currentTrigger==FlagTrigger.onspawn) {
                    builder.append(c);
                    String[] flag = builder.toString().split("=", 2);
                    HashMap<String, String> currentFlags = triggersWithFlags.get(currentTrigger);
                    currentFlags.put(flag[0], flag[1]);
                    triggersWithFlags.put(currentTrigger, currentFlags);
                    continue;
                }

                switch (c) {
                    case '<' -> inModifiers = true;
                    case '>' -> inModifiers = false;
                    case ']' -> {
                        String[] flag = builder.toString().split("=", 2);
                        Logger.log(triggersWithFlags);
                        HashMap<String, String> currentFlags = triggersWithFlags.get(currentTrigger);
                        currentFlags.put(flag[0], flag[1]);
                        triggersWithFlags.put(currentTrigger, currentFlags);
                        Logger.log(triggersWithFlags);

                        builder = new StringBuilder();
                        currentTrigger = FlagTrigger.onspawn;
                        continue;
                    }
                    case '[' -> {
                        String id = builder.substring(0, builder.length() - 1).toLowerCase();
                        if(EnumUtils.isATrigger(id)) {
                            currentTrigger = FlagTrigger.valueOf(id);
                            triggersWithFlags.putIfAbsent(currentTrigger, new HashMap<>());
                        }
                        builder = new StringBuilder();
                        continue;
                    }
                    case ';' -> {
                        if(!inModifiers) {
                            String[] flag = builder.toString().split("=", 2);
                            HashMap<String, String> currentFlags = triggersWithFlags.get(currentTrigger);
                            currentFlags.put(flag[0], flag[1]);
                            triggersWithFlags.put(currentTrigger, currentFlags);

                            builder = new StringBuilder();
                            continue;
                        }
                    }
                }

                builder.append(c);

            }
        }

        pack.parseRawFlags(triggersWithFlags);
        return pack;
    }

    public static class FlagString {

        public String flagSection;
        public String outerSection;
        public String item;
        public String origin;

        public FlagString(String str) {
            int flagStart = str.indexOf("{");
            int flagEnd = str.lastIndexOf("}");
            int space = str.indexOf(" ");

            if(flagStart!=-1 && flagEnd!=-1) {
                flagSection = str.substring(flagStart+1, flagEnd);
            }

            int outerStart = Math.max(space, flagEnd);
            if(outerStart!=-1) {
                outerSection = str.substring(outerStart+1).strip();
                if(outerSection.contains(";")) {
                    outerSection = outerSection.substring(0 ,outerSection.indexOf(";"));
                }
            } else if(str.contains(";")) {
                str = str.substring(0 ,str.indexOf(";"));
            }

            String itemPart;
            int itemEnd = space;
            if(flagStart!=-1) itemEnd = flagStart;
            if(itemEnd!=-1) {
                itemPart = str.substring(0, itemEnd);
                String[] splitResult = itemPart.split(":");
                if(splitResult.length==1) item = splitResult[0];
                else {
                    origin = splitResult[0];
                    item = splitResult[1];
                }
            } else {
                String[] splitResult = str.split(":");
                if(splitResult.length==1) item = splitResult[0];
                else {
                    origin = splitResult[0];
                    item = splitResult[1];
                }
            }

        }

    }

}
