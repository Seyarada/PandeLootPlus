package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collections;

public class MythicMobsMechanic extends SkillMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {


    public MythicMobsMechanic(MythicLineConfig config) {
        super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
    }

    @Override
    public boolean castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        String i = config.getLine();
        int j = i.indexOf("{")+1;
        int k = i.lastIndexOf("}");
        String dropString = i.substring(j, k);

        IDrop iDrop = IDrop.getAsDrop(dropString, null);
        new LootDrop(Collections.singletonList(iDrop), null, BukkitAdapter.adapt(abstractLocation))
                .build()
                .drop();
        return true;
    }

    @Override
    public boolean castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
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
        return true;
    }
}