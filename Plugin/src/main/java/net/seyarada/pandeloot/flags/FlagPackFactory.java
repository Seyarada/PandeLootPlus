package net.seyarada.pandeloot.flags;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.EnumUtils;

import java.util.Arrays;
import java.util.HashMap;

public class FlagPackFactory {

    public static FlagPack getPack(String str) {
        int brLoc = str.indexOf("{");
        int bfLoc = Math.max(str.lastIndexOf("}"), 0);
        String flagsSection = str.substring(brLoc+1, bfLoc)+"}";
        String outerSection = str.substring(bfLoc).strip();

        Logger.log("Flag Section %s", flagsSection);
        Logger.log("Outer Section %s", outerSection);

        HashMap<FlagTrigger, HashMap<String, String>> unTypedPack = new HashMap<>();
        FlagPack pack = new FlagPack();

        if(!outerSection.isEmpty()) {
            HashMap<String, String> map = new HashMap<>();
            Arrays.stream(outerSection.split(" ")).forEach(element -> {
                if(element.contains(".")) {
                    map.put("chance", element);
                } else {
                    map.put("amount", element);
                }
            });
            unTypedPack.put(FlagTrigger.onspawn, map);
        }

        StringBuilder builder = new StringBuilder();
        FlagTrigger readingFor = FlagTrigger.onspawn;
        String flagWaitingForData = null;
        boolean inModifiers = false;

        long bracketCount = flagsSection.chars().filter(ch -> ch == '}').count();
        long visitedBrackets = 0;

        for(int i = 0; i<flagsSection.length(); i++) {
            char c = flagsSection.charAt(i);

            if(c == '}') visitedBrackets++;

            // End of the flag part
            if(visitedBrackets==bracketCount || i==flagsSection.length()-1) {
                pack.writeRawFlagToMap(unTypedPack, readingFor, flagWaitingForData, builder.toString());
                visitedBrackets++;
                builder = new StringBuilder();
                continue;
            }

            switch (c) {
                case '<' -> inModifiers = true;
                case '>' -> inModifiers = false;
                case ']' -> {
                    pack.writeRawFlagToMap(unTypedPack, readingFor, flagWaitingForData, builder.toString());
                    builder = new StringBuilder();
                    flagWaitingForData = null;
                    readingFor = FlagTrigger.onspawn;
                    continue;
                }
                case ';' -> {
                    if(inModifiers) break;

                    if(flagWaitingForData!=null)
                        pack.writeRawFlagToMap(unTypedPack, readingFor, flagWaitingForData, builder.toString());
                    builder = new StringBuilder();
                    continue;
                }
                case '=' -> {
                    if(inModifiers) break;

                    String id = builder.toString().toLowerCase();

                    if(EnumUtils.isATrigger(id)) {
                        readingFor = FlagTrigger.valueOf(id);
                        pack.flags.put(readingFor, new HashMap<>());
                        i++;
                    } else {
                        flagWaitingForData = id;
                    }

                    builder = new StringBuilder();
                    continue;
                }
            }

            builder.append(c);
        }

        pack.parseRawFlags(unTypedPack);
        return pack;
    }

}
