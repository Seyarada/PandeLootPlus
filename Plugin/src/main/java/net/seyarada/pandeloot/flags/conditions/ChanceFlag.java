package net.seyarada.pandeloot.flags.conditions;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import net.seyarada.pandeloot.utils.MathUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@FlagEffect(id="chance", description="Determines the active color of a drop")
public class ChanceFlag implements ICondition {

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        return Math.random()<=MathUtils.eval(lootDrop.parse(values.getString()));
    }

    public static double getChance(FlagPack.FlagModifiers values, LootDrop lootDrop) {
        return MathUtils.eval(lootDrop.parse(values.getString()));
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return Math.random()<=MathUtils.eval(values.getString());
    }

}
