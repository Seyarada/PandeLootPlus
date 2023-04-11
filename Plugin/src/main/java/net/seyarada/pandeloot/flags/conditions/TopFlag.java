package net.seyarada.pandeloot.flags.conditions;

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
        if(lootDrop.p==null) return false;
        if(lootDrop.damageBoard==null) return false;

        DamageBoard dB = lootDrop.damageBoard;
        UUID uuid = lootDrop.p.getUniqueId();

        if(values.getString().contains("to")) {
            String[] v = values.getString().split("to");
            return Integer.parseInt(v[0]) <= dB.playerRanks.indexOf(uuid)+1 &&
                    Integer.parseInt(v[1]) >= dB.playerRanks.indexOf(uuid)+1;
        }

        int intTop = Integer.parseInt(values.getString())-1;
        if(intTop<0) return false;

        return dB.playerRanks.size() > intTop && dB.playerRanks.get(intTop) == uuid;
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return true;
    }

}
