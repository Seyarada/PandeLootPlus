package net.seyarada.pandeloot.compatibility.citizens;

import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Player;

public class CitizensCompatibility {

    public static boolean enabled = false;

    public static boolean isFromCitizens(Player player) {
        if(!enabled) return false;
        return CitizensAPI.getNPCRegistry().isNPC(player);
    }

}
