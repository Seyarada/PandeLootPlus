package net.seyarada.pandeloot.flags.conditions;

import com.google.common.collect.Sets;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.ICondition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;

@FlagEffect(id="mmcondition", description="Determines the active color of a drop")
public class MythicConditionFlag implements ICondition {

    boolean passesCheck(Player player, Entity source, FlagPack.FlagModifiers values) {
        String eval = values.getOrDefault("eval", "player");

        switch (eval) {
            case "mob" -> {
                AbstractEntity mob = BukkitAdapter.adapt(source);
                GenericCaster mobCaster = new GenericCaster(mob);
                AbstractEntity playerTarget = BukkitAdapter.adapt(player);
                HashSet<AbstractEntity> targets = Sets.newHashSet();
                targets.add(playerTarget);
                SkillMetadata meta = new SkillMetadataImpl(SkillTriggers.API, mobCaster, mob, mob.getLocation(), targets, null, 1);
                return MythicBukkit.inst().getSkillManager().getCondition(values.getString()).evaluateCaster(meta);
            }
            case "player" -> {
                AbstractEntity pAbstract = BukkitAdapter.adapt(player);
                GenericCaster playerCaster = new GenericCaster(pAbstract);
                AbstractEntity mobTarget = BukkitAdapter.adapt(source);
                HashSet<AbstractEntity> targets = Sets.newHashSet();
                targets.add(mobTarget);
                SkillMetadata meta = new SkillMetadataImpl(SkillTriggers.API, playerCaster, pAbstract, pAbstract.getLocation(), targets, null, 1);
                return MythicBukkit.inst().getSkillManager().getCondition(values.getString()).evaluateCaster(meta);
            }
        }
        return true;
    }

    @Override
    public boolean onCheck(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop itemDrop) {
        if(lootDrop.p==null) return true;
        return passesCheck(lootDrop.p, lootDrop.sourceEntity, values);
    }

    @Override
    public boolean onCheckNoLootDrop(FlagPack.FlagModifiers values, Entity entity, Player player) {
        return passesCheck(player, entity, values);
    }

}
