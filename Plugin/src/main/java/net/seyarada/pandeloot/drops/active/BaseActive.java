package net.seyarada.pandeloot.drops.active;

import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.nms.NMSManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseActive {

    // region Holograms
    List<Entity> holograms;
    List<Player> hologramsPlayers;

    public void startHologramRunnable(Entity e, List<Entity> holograms, List<Player> players) {
        this.holograms = holograms;
        this.hologramsPlayers = players;

        final Location[] oldLoc = {e.getLocation()};
        addTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid()) cancelTasks();
            if(oldLoc[0].equals(e.getLocation())) return;
            oldLoc[0] = e.getLocation();

            Location tempLoc = oldLoc[0].clone();

            tempLoc.add(0,0.25,0);
            for (Entity i : holograms) {
                tempLoc.add(0,0.22,0);
                if (i == null) continue;
                for (Player player : players) {
                    NMSManager.get().updateHologramPosition(tempLoc.getX(), tempLoc.getY(), tempLoc.getZ(), i, player);
                }
            }

        }, 0, 1));
    }

    // endregion

    //region Bukkit Tasks
    final HashMap<Integer, BukkitTask> bukkitTasks = new HashMap<>();

    void addTask(int id) {
        bukkitTasks.put(id, BukkitTask.GENERIC);
    }

    void addTask(BukkitTask task, int id) {
        bukkitTasks.put(id, task);
    }

    boolean hasTask(BukkitTask task) {
        return bukkitTasks.containsValue(task);
    }

    void stopTask(BukkitTask taskToStop) {
        bukkitTasks.entrySet().stream()
                .filter(entry -> entry.getValue() == taskToStop)
                .forEach(entry -> Bukkit.getScheduler().cancelTask(entry.getKey()));
        bukkitTasks.entrySet().removeIf(entry -> entry.getValue() == taskToStop);
    }

    void cancelTasks() {
        final BukkitScheduler scheduler = Bukkit.getScheduler();

        bukkitTasks.keySet().forEach(scheduler::cancelTask);

        for (Player player : hologramsPlayers) {
            if (!player.isOnline()) continue;
            holograms.stream()
                    .filter(Objects::nonNull)
                    .forEach(e -> NMSManager.get().destroyEntity(e.getEntityId(), player));
        }
    }

    enum BukkitTask {
        ONLAND,
        RAINBOW,
        BEAM,
        LOOTBAG_ROLLER,
        HOLOGRAM,
        VOID_PROTECTION,
        MAGNET,
        FLYING_PARTICLE,
        GENERIC
    }
    //endregion


}
