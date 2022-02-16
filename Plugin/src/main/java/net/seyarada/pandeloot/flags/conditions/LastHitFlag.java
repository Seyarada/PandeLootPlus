package net.seyarada.pandeloot.flags.conditions;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@FlagEffect(id="lasthit", description="Determines the active color of a drop")
public class LastHitFlag implements ICondition {

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        if(lootDrop.p==null) return true;
        if(values.getBoolean()) {
            return lootDrop.damageBoard.lastHit.getUniqueId()==lootDrop.p.getUniqueId();
        }
        return true;
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return true;
    }

}
