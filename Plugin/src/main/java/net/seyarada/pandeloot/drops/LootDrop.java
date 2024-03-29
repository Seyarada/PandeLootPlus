package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.config.Boosts;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.config.Pity;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.drops.containers.LootBag;
import net.seyarada.pandeloot.flags.effects.AmountFlag;
import net.seyarada.pandeloot.flags.effects.DelayFlag;
import net.seyarada.pandeloot.flags.effects.SkipFlag;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.trackers.DamageBoard;
import net.seyarada.pandeloot.utils.ChatUtils;
import net.seyarada.pandeloot.utils.MathUtils;
import net.seyarada.pandeloot.utils.StringParser;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LootDrop {

    ArrayList<IDrop> itemDrops;
    List<String> dropStrings;
    public final Player p;
    public final Location l;
    public Entity dropEntity;

    public DamageBoard damageBoard;
    public Entity sourceEntity;

    // Flags things
    public boolean continueDrops = true;
    long totalDelay = 0;

    boolean arePlaceholdersLoaded = false;
    HashMap<String, String> placeholderValues = new HashMap<>();
    public StrSubstitutor sub;

    List<IDrop> baseDropList;

    // This map is for flags to store information for following executions of such flag
    // For example, radial exploding knowing the order of the drop
    public final HashMap<String, String> data = new HashMap<>();

    public LootDrop(ArrayList<IDrop> dropList, Player p, Location l) {
        this.p = p;
        this.l = l;
        this.baseDropList = dropList;
    }

    public LootDrop(List<String> stringList, Player player, Location l) {
        this.p = player;
        this.l = l;
        this.dropStrings = stringList;
    }

    public LootDrop(String str, Player player, Location l) {
        this.p = player;
        this.l = l;
        this.dropStrings = Collections.singletonList(str);
    }

    public LootDrop(IDrop drop, Player player, Location l) {
        this.p = player;
        this.l = l;
        this.baseDropList = Collections.singletonList(drop);
    }

    public LootDrop build() {
        loadPlaceholders();
        if(baseDropList!=null && baseDropList.size()>0)
            itemDrops = collectDrops(baseDropList, false);
        else
            itemDrops = collectDrops(IDrop.getAsDrop(dropStrings, p, this), false);
        return this;
    }

    public int size() {
        return baseDropList.size();
    }

    public LootDrop setDamageBoard(DamageBoard dB) {
        this.damageBoard = dB;
        return this;
    }

    public LootDrop setSourceEntity(Entity e) {
        this.sourceEntity = e;
        return this;
    }

    public void loadPlaceholders() {
        if(arePlaceholdersLoaded) return;
        arePlaceholdersLoaded = true;

        double globalBoost = Boosts.getBoost("Global");
        double boost = globalBoost;

        if(p!=null) {
            String sUUID = p.getUniqueId().toString();
            placeholderValues.put("player", p.getName());
            placeholderValues.put("player.name", p.getDisplayName());
            placeholderValues.put("player.uuid", sUUID);
            placeholderValues.put("player.boost", String.valueOf(Boosts.getBoost(sUUID)));
            placeholderValues.put("player.pity", String.valueOf(Pity.getPity(p, "Global")));
            if(Pity.pity.containsKey(p.getUniqueId().toString())) {
                Pity.pity.get(p.getUniqueId().toString()).forEach((id, pity) -> {
                    placeholderValues.put("player.pity."+id, String.valueOf(pity));
                });
            }
            boost = Math.max(Boosts.getBoost(sUUID), globalBoost);
        }

        placeholderValues.put("global.boost", String.valueOf(globalBoost));
        placeholderValues.put("boost", String.valueOf(boost));

        if(damageBoard!=null) {
            placeholderValues.putAll(damageBoard.placeholders);
            if(p!=null) {
                int playerRank = damageBoard.playerRanks.indexOf(p.getUniqueId());
                double playerDamage = damageBoard.playerDamages.get(playerRank);
                placeholderValues.put("player.rank", String.valueOf(playerRank+1));
                placeholderValues.put("player.damage", MathUtils.dd.format(playerDamage));
                placeholderValues.put("player.dmg", MathUtils.dd.format(playerDamage));
                placeholderValues.put("player.percent", damageBoard.getPercent(playerRank, false));
                placeholderValues.put("player.ratio", damageBoard.getPercent(playerRank, true));
            }

        }
        sub = new StrSubstitutor(placeholderValues, "%", "%");
    }

    public void drop() {
        Logger.log("Doing drops for %s", p);
        int skip = 0;
        for(IDrop drop : itemDrops) {
            if(!continueDrops) break;
            if(skip>0) {
                skip--;
                continue;
            }

            if(drop.getFlagPack().hasFlag(DelayFlag.class)) {
                totalDelay += drop.getFlagPack().getFlag(DelayFlag.class).getLong();
            }

            if(drop.getFlagPack().hasFlag(SkipFlag.class)) {
                skip = drop.getFlagPack().getFlag(SkipFlag.class).getInt();
            }

            Logger.log("Running drop %s with flags %s", drop, drop.getFlagPack());
            if(totalDelay>0)
                Bukkit.getScheduler().runTaskLater(PandeLoot.inst, () -> drop.run(this), totalDelay);
            else
                drop.run(this);
            }
    }

    public String substitutor(String text) {
        loadPlaceholders();
        return StringParser.parseText(text, this);
    }

    public ArrayList<IDrop> collectDrops(List<IDrop> drops, boolean bypass) {
        ArrayList<IDrop> toDrop = new ArrayList<>();
        for(IDrop drop : drops) {
            Logger.log("Collecting %s", drop);
            if(!bypass && !drop.passesConditions(this)) {
                Logger.log("Drop failed conditions");
                continue;
            }
            if(drop instanceof ItemDrop || drop instanceof EntityDrop) {
                toDrop.add(drop);
            } else if(drop instanceof LootBag bag) {
                int amount = 1;
                if(drop.getFlagPack().hasFlag(AmountFlag.class)) {
                    amount = AmountFlag.getValueFromRanged(drop.getFlagPack().getFlag(AmountFlag.class).getString());
                }
                for (int i = 0; i < amount; i++) {
                    toDrop.add(bag.getDrop(drop.getFlagPack()));
                }
            } else if(drop instanceof IContainer itemContainer) {
                int amount = 1;
                if(drop.getFlagPack().hasFlag(AmountFlag.class)) {
                    amount = AmountFlag.getValueFromRanged(drop.getFlagPack().getFlag(AmountFlag.class).getString());
                }
                for (int i = 0; i < amount; i++) {
                    ArrayList<IDrop> finalToAdd = collectDrops(itemContainer.getDropList(this), true);
                    toDrop.addAll(finalToAdd);
                }
            }
        }

        return toDrop;
    }

    public void displayScoreHolograms() {
        if(p==null) return;
        Location loc = (l==null) ? p.getLocation() : l;

        List<String> lines = new ArrayList<>();
        Config.getScoreHologram().forEach(msg -> {
            msg = substitutor(msg);
            if(msg!=null) {
                lines.add(msg);
            }
        });

        NMSManager.get().hologram(400, loc, this.p, lines, PandeLoot.inst);
    }

    public void displayScoreMessage() {
        if(p==null) return;
        Config.getScoreMessage().forEach(msg -> {
            msg = substitutor(msg);
            if(msg!=null) ChatUtils.sendCenteredMessage(p, msg);
        });
    }

    public Location getLocation() {
        if (l != null)
            return l;
         if (p != null)
            return p.getLocation();
        return null;
    }

}
