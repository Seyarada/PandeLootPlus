package net.seyarada.pandeloot.flags.conditions;

import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@FlagEffect(id="permission", description="Determines the active color of a drop")
public class PermissionFlag implements ICondition {

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        if(lootDrop.p==null) return true;
        return lootDrop.p.hasPermission(values.getString());
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return true;
    }

}
