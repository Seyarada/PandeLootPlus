package net.seyarada.pandeloot.trackers;

import net.seyarada.pandeloot.compatibility.citizens.CitizensCompatibility;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.*;

public class DamageTracker implements Listener {

    //                 Mob UUID       Player  P.Damage
    public static HashMap<UUID, HashMap<UUID, Double>> mobsDamageMap = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamaged(EntityDamageByEntityEvent e) {
        UUID mob = e.getEntity().getUniqueId();
        if(!mobsDamageMap.containsKey(mob)) return;

        Player player = null;
        if(e.getDamager() instanceof Player p)
            player = p;
        else if(e.getDamager() instanceof Projectile projectile)
            if (projectile.getShooter() instanceof Player p)
                player = p;
        if(player==null) return;
        if(CitizensCompatibility.isFromCitizens(player)) return;

        addPlayerDamage(mob, player, e.getFinalDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(EntitySpawnEvent e) {
        ConfigurationSection config = Config.getMob(e.getEntity());
        if(config!=null) {
            boolean shouldTrack = config.contains("Rewards");
            if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreHologram");
            if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreMessage");

            if(shouldTrack) DamageTracker.initTracking(e.getEntity().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent e) {
        UUID mob = e.getEntity().getUniqueId();
        if(!DamageTracker.has(mob)) return;

        ConfigurationSection config = Config.getMob(e.getEntity());
        if(config==null) return;
        boolean scoreMessage = config.getBoolean("Options.ScoreMessage");
        boolean scoreHologram = config.getBoolean("Options.ScoreHologram");

        List<String> strings = config.getStringList("Rewards");
        ArrayList<IDrop> itemsToDrop = IDrop.getAsDrop(strings);

        DamageBoard damageBoard = new DamageBoard(e.getEntity(), DamageTracker.mobsDamageMap.get(mob));

        for(UUID uuid : DamageTracker.mobsDamageMap.get(mob).keySet()) {
            LootDrop lootDrop = new LootDrop(itemsToDrop, Bukkit.getPlayer(uuid), e.getEntity().getLocation())
                    .setDamageBoard(damageBoard)
                    .setSourceEntity(e.getEntity())
                    .build();

            if(scoreHologram) lootDrop.displayScoreHolograms();
            if(scoreMessage) lootDrop.displayScoreMessage();

            lootDrop.drop();
        }

        remove(mob);
    }

    public static void initTracking(UUID mob) {
        mobsDamageMap.put(mob, new HashMap<>());
    }

    public static boolean has(UUID mob) {
        return mobsDamageMap.containsKey(mob) && mobsDamageMap.get(mob).size() != 0;
    }

    public static void remove(UUID mob) {
        mobsDamageMap.remove(mob);
    }

    public static void addPlayerDamage(UUID mob, Player player, double damage) {
        LivingEntity livingEntity = (LivingEntity) Bukkit.getEntity(mob);
        damage = Math.min(livingEntity.getHealth(), damage);

        double oldDamage = mobsDamageMap.get(mob).getOrDefault(player.getUniqueId(), 0d);
        mobsDamageMap.get(mob).put(player.getUniqueId(), oldDamage+damage);
    }

    public static class DamageBoard {

        public double damageReceived;
        public double mobHP;
        UUID mobUUID;

        public Entity lastHit;

        public LinkedList<Map.Entry<UUID, Double>> sortedPlayers;
        public LinkedList<UUID> playerRanks = new LinkedList<>();
        public LinkedList<Double> playerDamages = new LinkedList<>();
        Comparator<Map.Entry<UUID, Double>> comparator = Map.Entry.comparingByValue();
        public HashMap<String, String> placeholders = new HashMap<>();

        public DamageBoard(LivingEntity mob, HashMap<UUID, Double> players) {
            damageReceived = players.values().stream().mapToDouble(Double::valueOf).sum();
            mobHP = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            mobUUID = mob.getUniqueId();
            sort(players);

            placeholders.put("mob.uuid", mobUUID.toString());
            placeholders.put("mob.hp", String.valueOf((int)mobHP));
            placeholders.put("mob.tanked", String.valueOf((int)damageReceived));
            placeholders.put("mob.name", (mob.isCustomNameVisible()) ? mob.getCustomName() : mob.getName());

            int i = 1;
            for(Map.Entry<UUID, Double> entry : sortedPlayers) {
                playerRanks.add(entry.getKey());
                playerDamages.add(entry.getValue());
                Player p = Bukkit.getPlayer(entry.getKey());
                String visualDamage = MathUtils.dd.format(entry.getValue());
                placeholders.put(i+".name", p.getDisplayName());
                placeholders.put(i+".damage", visualDamage);
                placeholders.put(i+".percent", getPercent(i-1, false));
                placeholders.put(i+".ratio", getPercent(i-1, true));
                i++;
            }
        }

        public String getPercent(int rank, boolean isRatio) {
            int ratio = (isRatio) ? 1 : 100;
            return String.valueOf(MathUtils.dd.format( playerDamages.get(rank)/damageReceived * ratio ));
        }

        void sort(HashMap<UUID, Double> players) {
            sortedPlayers = new LinkedList<>(players.entrySet());
            sortedPlayers.sort(comparator.reversed());
        }

    }

}


























