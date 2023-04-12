package net.seyarada.pandeloot.flags.conditions;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import net.seyarada.pandeloot.trackers.DamageBoard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

@FlagEffect(id="top", description="Determines the active color of a drop")
public class TopFlag implements ICondition {

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        Logger.log("Checking Top flag for "+lootDrop.p);
        if(lootDrop.p==null) return false;
        if(lootDrop.damageBoard==null) return false;

        DamageBoard dB = lootDrop.damageBoard;
        UUID uuid = lootDrop.p.getUniqueId();
        Logger.log("Player UUID: "+uuid);
        Logger.log("Top List: "+dB.playerRanks);
        Logger.log("Top List Damages: "+dB.playersAndDamage);
        Logger.log("Top flag value: "+values.toString());

        if(values.getString().contains("to")) {
            String[] v = values.getString().split("to");
            return Integer.parseInt(v[0]) <= dB.playerRanks.indexOf(uuid)+1 &&
                    Integer.parseInt(v[1]) >= dB.playerRanks.indexOf(uuid)+1;
        }

        int intTop = Integer.parseInt(values.getString())-1;

        Logger.log("intTop: "+intTop);
        if(dB.playerRanks.size() > intTop) {
            Logger.log("Player at rank: "+dB.playerRanks.get(intTop));
            Logger.log("This player: "+uuid);
        }

        if(intTop<0) return false;

        return dB.playerRanks.size() > intTop && dB.playerRanks.get(intTop) == uuid;
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        Logger.log("Checking with no lootDrop for "+player);
        Logger.log("Checking with no lootDrop value "+values.getString());
        return false;
    }

}
