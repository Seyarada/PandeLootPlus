package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillMechanic;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MythicMobsMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {

    final MythicLineConfig config;

    public MythicMobsMechanic(MythicLineConfig config) {
        this.config = config;
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        String dropString = i.substring(j, k);

        IDrop iDrop = IDrop.getAsDrop(dropString, null);
        new LootDrop(Collections.singletonList(iDrop), null, BukkitAdapter.adapt(abstractLocation))
                .build()
                .drop();
        return SkillResult.SUCCESS;
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        Entity entity = abstractEntity.getBukkitEntity();
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        String dropString = i.substring(j, k);

        if(entity instanceof Player player) {
            IDrop iDrop = IDrop.getAsDrop(dropString, player);
            new LootDrop(Collections.singletonList(iDrop), player, BukkitAdapter.adapt(skillMetadata.getOrigin()))
                    .build()
                    .drop();
        } else {
            IDrop iDrop = IDrop.getAsDrop(dropString, null);
            new LootDrop(Collections.singletonList(iDrop), null, BukkitAdapter.adapt(skillMetadata.getOrigin()))
                    .build()
                    .drop();
        }
        return SkillResult.SUCCESS;
    }
}