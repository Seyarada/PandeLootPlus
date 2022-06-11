package net.seyarada.pandeloot.compatibility.mythicmobs;

import com.google.common.collect.Sets;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.core.skills.placeholders.parsers.PlaceholderStringImpl;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Optional;

public class MythicMobsCompatibility {

    public static ItemStack getItem(String item) {
        Optional<MythicItem> mI = MythicBukkit.inst().getItemManager().getItem(item);
        return mI.map(mythicItem -> BukkitAdapter.adapt(mythicItem.generateItemStack(1))).orElse(null);
    }

    public static String parse(String text, LootDrop drop, Player player) {
        AbstractEntity mob = BukkitAdapter.adapt(drop.sourceEntity);
        Optional<ActiveMob> optionalMob = MythicBukkit.inst().getMobManager().getActiveMob(drop.sourceEntity.getUniqueId());
        if(optionalMob.isPresent()) {
            ActiveMob mobCaster = optionalMob.get();
            AbstractEntity playerTarget = BukkitAdapter.adapt(player);
            HashSet<AbstractEntity> targets = Sets.newHashSet();
            targets.add(playerTarget);
            SkillMetadata meta = new SkillMetadataImpl(SkillTriggers.API, mobCaster, mob, mob.getLocation(), targets, null, 1);
            return new PlaceholderStringImpl(text).get(meta, playerTarget);
        }
        return text;
    }



}
