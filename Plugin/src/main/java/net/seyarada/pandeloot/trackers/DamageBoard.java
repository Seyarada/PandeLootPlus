package net.seyarada.pandeloot.trackers;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class DamageBoard {

    public static HashMap<UUID, DamageBoard> damageBoards = new HashMap<>();

    public HashMap<UUID, Double> playersAndDamage = new HashMap<>();

    protected UUID mobUUID;
    protected LivingEntity mobLiving;
    public double baseHealth;

    public UUID lastHit;
    public UUID firstHit;
    public double damageReceived;

    public LinkedList<UUID> playerRanks = new LinkedList<>();
    public LinkedList<Double> playerDamages = new LinkedList<>();
    public HashMap<String, String> placeholders = new HashMap<>();

    LinkedList<Map.Entry<UUID, Double>> sortedPlayers;
    Comparator<Map.Entry<UUID, Double>> comparator = Map.Entry.comparingByValue();

    public DamageBoard(LivingEntity mob) {
        this.mobUUID = mob.getUniqueId();
        this.mobLiving = mob;
        damageBoards.put(mobUUID, this);
    }

    public void compileInformation() {
        baseHealth = mobLiving.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        damageReceived = playersAndDamage.values().stream().mapToDouble(Double::valueOf).sum();
        sort(playersAndDamage);

        placeholders.put("mob.uuid", mobUUID.toString());
        placeholders.put("mob.hp", String.valueOf((int)baseHealth));
        placeholders.put("mob.tanked", String.valueOf((int)damageReceived));
        placeholders.put("mob.name", (mobLiving.isCustomNameVisible()) ? mobLiving.getCustomName() : mobLiving.getName());

        int i = 1;
        for(Map.Entry<UUID, Double> entry : sortedPlayers) {
            playerRanks.add(entry.getKey());
            playerDamages.add(entry.getValue());
            Player p = Bukkit.getPlayer(entry.getKey());
            if(p!=null)
                placeholders.put(i+".name", p.getDisplayName());
            String visualDamage = MathUtils.dd.format(entry.getValue());
            placeholders.put(i+".damage", visualDamage);
            placeholders.put(i+".dmg", getPercent(i-1, false));
            placeholders.put(i+".percent", getPercent(i-1, false));
            placeholders.put(i+".ratio", getPercent(i-1, true));
            i++;
        }

        if(Config.debug) Logger.log("Damage Board Results:");
        if(Config.debug) Logger.log("  "+playersAndDamage);
    }

    public String getPercent(int rank, boolean isRatio) {
        int ratio = (isRatio) ? 1 : 100;
        return String.valueOf(MathUtils.dd.format( playerDamages.get(rank)/damageReceived * ratio ));
    }

    void sort(HashMap<UUID, Double> players) {
        sortedPlayers = new LinkedList<>(players.entrySet());
        sortedPlayers.sort(comparator.reversed());
    }

    public static void addPlayerDamage(UUID damagedEntity, Player player, double damage) {
        if(Bukkit.getEntity(damagedEntity) instanceof LivingEntity mob) {
            damage = Math.min(mob.getHealth(), damage);
            UUID playerUUID = player.getUniqueId();

            DamageBoard board = damageBoards.get(damagedEntity);
            board.playersAndDamage.merge(playerUUID, damage, Double::sum);

            board.lastHit = playerUUID;
            if(board.firstHit==null) board.firstHit = playerUUID;
        }
    }

    public static boolean contains(UUID uuid) {
        return damageBoards.containsKey(uuid);
    }

    public static DamageBoard get(UUID uuid) {
        return damageBoards.get(uuid);
    }

    public static void remove(UUID uuid) {
        damageBoards.remove(uuid);
    }


}