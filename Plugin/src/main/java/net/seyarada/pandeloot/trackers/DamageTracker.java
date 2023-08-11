package net.seyarada.pandeloot.trackers;

import net.seyarada.pandeloot.compatibility.citizens.CitizensCompatibility;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.List;
import java.util.UUID;

public class DamageTracker implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamaged(EntityDamageByEntityEvent e) {
        UUID mob = e.getEntity().getUniqueId();
        if(!DamageBoard.contains(mob)) return;

        Player player = null;
        if(e.getDamager() instanceof Player p)
            player = p;
        else if(e.getDamager() instanceof Projectile projectile)
            if (projectile.getShooter() instanceof Player p)
                player = p;
        if(player==null) return;
        if(CitizensCompatibility.isFromCitizens(player)) return;

        DamageBoard.addPlayerDamage(mob, player, e.getFinalDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(EntitySpawnEvent e) {
        if(e.getEntity() instanceof LivingEntity entity) {
            ConfigurationSection config = Config.getMob(entity);
            if(config!=null) {
                boolean shouldTrack = config.contains("Rewards");
                if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreHologram");
                if(!shouldTrack) shouldTrack = config.getBoolean("Options.ScoreMessage");

                if(shouldTrack) new DamageBoard(entity);
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(EntityDeathEvent e) {
        UUID mob = e.getEntity().getUniqueId();
        if(!DamageBoard.contains(mob)) return;

        ConfigurationSection config = Config.getMob(e.getEntity());
        if(config==null) return;
        boolean scoreMessage = config.getBoolean("Options.ScoreMessage");
        boolean scoreHologram = config.getBoolean("Options.ScoreHologram");

        List<String> strings = config.getStringList("Rewards");

        DamageBoard damageBoard = DamageBoard.get(mob);
        damageBoard.compileInformation();


        for(UUID uuid : damageBoard.playersAndDamage.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            LootDrop lootDrop = new LootDrop(strings, player, e.getEntity().getLocation())
                    .setDamageBoard(damageBoard)
                    .setSourceEntity(e.getEntity())
                    .build();

            if(scoreHologram) lootDrop.displayScoreHolograms();
            if(scoreMessage) lootDrop.displayScoreMessage();

            lootDrop.drop();
        }

        DamageBoard.remove(mob);
    }


}