package net.seyarada.pandeloot.drops.active;

import net.md_5.bungee.api.ChatColor;
import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.GlowFlag;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemActive extends BaseActive {

    public static HashMap<Entity, ItemActive> activeDropItem = new HashMap<>();

    public boolean canBePickedUp = true;

    Entity e;
    Player p;
    FlagPack flags;
    LootDrop lootDrop;
    IDrop iDrop;

    Location origin;

    public ItemActive(IDrop drop, Entity e, Player p, FlagPack pack, LootDrop lootDrop) {
        this.e = e;
        this.p = p;
        this.flags = drop.getFlagPack();
        this.lootDrop = lootDrop;
        this.iDrop = drop;

        if(e!=null) {
            origin = e.getLocation();
        }

        activeDropItem.put(e, this);
        pack.trigger(FlagTrigger.onspawn, e, lootDrop, drop);

        if(pack.flags.containsKey(FlagTrigger.onland))
            checkForLandings(e, pack);
    }

    public void trigger(FlagTrigger trigger) {
        flags.trigger(trigger, e, lootDrop, iDrop);
    }

    void checkForLandings(Entity i, FlagPack pack) {
        AtomicBoolean hasLanded = new AtomicBoolean(false);

        addTask(BukkitTask.ONLAND, Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!i.isValid()) stopTask(BukkitTask.ONLAND);

            if(i.isOnGround() && !hasLanded.get()) {
                ItemActive activeDrop = ItemActive.get(i);
                hasLanded.set(true);
                pack.trigger(FlagTrigger.onland, i, activeDrop.lootDrop, activeDrop.iDrop);
            } else if(!i.isOnGround()) {
                hasLanded.set(false);
            }

        }, 0, 3));
    }

    public void startMagnetRunnable(double force, double distanceTrigger, int frequency) {
        addTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid() || lootDrop.p==null ) cancelTasks();

            Location pL = lootDrop.p.getLocation().clone().add(0,1,0);

            double distance = e.getLocation().distance(pL);
            if(distance<=distanceTrigger) {
                // Check if player's inventory is full
                if(lootDrop.p.getInventory().firstEmpty() == -1) {
                    // Player's inventory is full, do not apply magnet effect
                    return;
                }
                double distanceMod = (force>0) ? (distance*0.25)*force : 1;

                Vector v = pL.toVector().subtract(e.getLocation().toVector()).normalize().multiply(force);
                v.setX( (v.getX()*0.5)*distanceMod+v.getX()*0.5 );
                v.setY( (v.getY()*0.5)*distanceMod+v.getY()*0.5 );
                v.setZ( (v.getZ()*0.5)*distanceMod+v.getZ()*0.5 );

                if(v.length() > 4)	{
                    v = v.normalize().multiply(4);
                }

                if(Double.isNaN(v.getX())) v.setX(0);
                if(Double.isNaN(v.getY())) v.setY(0);
                if(Double.isNaN(v.getZ())) v.setZ(0);

                e.setVelocity(v);
            }

        }, 0, frequency));
    }

    static final Vector noVelocity = new Vector(0,0,0);
    public void startVoidProtectionRunnable(double limit, int frequency) {
        addTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid()) cancelTasks();

            if(e.getFallDistance() > limit) {
                e.setGravity(false);
                e.teleport(origin);
                e.setVelocity(noVelocity);
            }

        }, 0, frequency));
    }

    public static ItemActive get(Entity i) {
        return activeDropItem.get(i);
    }

    // region Color & Glow
    private ChatColor color = Constants.ACCENT;
    float rainbowDegrees = 0;

    public void setColor(ChatColor color) {
        this.color = color;
        updateColors();
    }
    public ChatColor getColor() {
        return color;
    }

    public void updateColors() {
        if(e.isGlowing()) {
            if(!hasTask(BukkitTask.FLYING_PARTICLE)) {
                int frequency = flags.getFlag(GlowFlag.class).getIntOrDefault("frequency", 2);
                startFlyingParticleRunnable(frequency);
            }
            ((GlowFlag) FlagManager.getFromID("glow")).updateColor(e, color, p);
        }
    }

    public void startRainbowRunnable(int frequency) {
        addTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid()) cancelTasks();

            rainbowDegrees += 0.05;
            color = ChatColor.of(Color.getHSBColor(rainbowDegrees, 0.5f, 1));

            updateColors();

        }, 0, frequency));
    }

    public void startBeamRunnable(double height, int frequency) {
        addTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid() || lootDrop.p==null ) cancelTasks();

            if(e.isOnGround()) {
                double modHeight = height;
                while(modHeight>0) {
                    Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue()), 1);
                    lootDrop.p.spawnParticle(Particle.REDSTONE, e.getLocation().add(0, 0.15+modHeight, 0), 1, dustOptions);
                    modHeight = modHeight - 0.1;
                }
            }

        }, 0, frequency));
    }

    public void startFlyingParticleRunnable(int frequency) {
        addTask(BukkitTask.FLYING_PARTICLE, Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid() || lootDrop.p==null ) cancelTasks();

            if(!e.isOnGround()) {
                Particle.DustOptions dustOptions = new Particle.DustOptions(org.bukkit.Color.fromRGB(color.getColor().getRed(), color.getColor().getGreen(), color.getColor().getBlue()), 1);
                lootDrop.p.spawnParticle(Particle.REDSTONE, e.getLocation(), 1, dustOptions);
            }

        }, 0, frequency));
    }
    // endregion

}
