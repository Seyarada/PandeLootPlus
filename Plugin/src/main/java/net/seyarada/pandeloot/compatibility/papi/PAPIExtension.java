package net.seyarada.pandeloot.compatibility.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.seyarada.pandeloot.trackers.DamageBoard;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class PAPIExtension  extends PlaceholderExpansion {

    @Override
    public boolean canRegister() {
        return true;
    }
    @NotNull
    @Override
    public String getAuthor() {
        return "Seyarada";
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "pandeloot";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        if(identifier.contains("_")) {
            String[] split = identifier.split("_");
            String mobUUID = split[0];
            String playerUUID = split[1];

            for (UUID uuid : DamageBoard.damageBoards.keySet()) {
                if(uuid.toString().equals(mobUUID)) {
                    DamageBoard board = DamageBoard.get(uuid);
                    for (Map.Entry<UUID, Double> entry : board.playersAndDamage.entrySet()) {
                        if(entry.getKey().toString().equals(playerUUID)) {
                            return String.valueOf(entry.getValue());
                        }
                    }
                }
            }
        } else {
            for (DamageBoard board : DamageBoard.damageBoards.values()) {
                for (Map.Entry<UUID, Double> entry : board.playersAndDamage.entrySet()) {
                    if(entry.getKey().toString().equals(identifier)) {
                        return String.valueOf(entry.getValue());
                    }
                }
            }
        }

        return null;
    }
}