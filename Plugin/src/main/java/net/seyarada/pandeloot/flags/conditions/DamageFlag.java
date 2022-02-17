package net.seyarada.pandeloot.flags.conditions;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import net.seyarada.pandeloot.trackers.DamageBoard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@FlagEffect(id="damage", description="Determines the active color of a drop")
public class DamageFlag implements ICondition {

    boolean passesCheck(LootDrop drop, Player player, FlagPack.FlagModifiers values) {
        DamageBoard dB = drop.damageBoard;
        if(dB==null) return true;

        String requiredDamage = values.getString();

        double totalHP = dB.damageReceived;

        int playerSlot = dB.playerRanks.indexOf(player.getUniqueId());
        double playerDamage = dB.playerDamages.get(playerSlot);

        if(requiredDamage.contains("%")){
            requiredDamage = requiredDamage.replace("%", "");
            if (requiredDamage.contains("to")) {
                return damageRanged(requiredDamage, playerDamage, totalHP, true);
            }
            return playerDamage / totalHP * 100 >= Double.parseDouble(requiredDamage.replace("%", ""));
        }

        if (requiredDamage.contains("to")) {
            return damageRanged(requiredDamage, playerDamage, totalHP, false);
        }
        else return playerDamage >= Double.parseDouble(requiredDamage);
    }

    private static boolean damageRanged(String damageString, double playerDamage, double totalHP, boolean isPercentage) {
        String[] values = damageString.split("to");
        double v0 = Double.parseDouble(values[0]);
        double v1 = Double.parseDouble(values[1]);
        if(isPercentage)
            return v0 <= playerDamage / totalHP * 100 &&
                    v1 >= playerDamage / totalHP * 100;
        return v0 <= playerDamage && v1 >= playerDamage;
    }

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        if(lootDrop.p==null) return true;
        return passesCheck(lootDrop, lootDrop.p, values);
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return true;
    }

}
