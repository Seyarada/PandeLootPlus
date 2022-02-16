package net.seyarada.pandeloot.flags.conditions;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.util.jnbt.CompoundTag;
import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FlagEffect(id="holding", description="Determines the active color of a drop")
public class HoldingFlag implements ICondition {

    boolean passesCheck (Player player, FlagPack.FlagModifiers values) {
        String type = values.getOrDefault("type", "vanilla");
        ItemStack iS = player.getInventory().getItemInMainHand();

        if(values.getInt("amount")!=0) {
            if(iS.getAmount()<values.getInt("amount"))
                return false;
        }

        switch (type) {
            case "vanilla" -> {
                return iS.getType()==Material.valueOf(values.getString().toUpperCase());
            }
            case "mythicmobs", "mm" -> {
                CompoundTag tag = MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(iS);
                if(tag.containsKey(Constants.MM)) {
                    return tag.getString(Constants.MM).equalsIgnoreCase(values.getString());
                } else return false;
            }
            default -> {
                return true;
            }
        }
    }

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        if(lootDrop.p==null) return true;
        return passesCheck(lootDrop.p, values);
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return passesCheck(player, values);
    }

}
