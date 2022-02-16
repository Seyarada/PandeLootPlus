package net.seyarada.pandeloot.flags.effects;

import io.lumine.xikage.mythicmobs.MythicMobs;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@FlagEffect(id="skill", description="Broadcast a message")
public class MythicSkillFlag implements IGeneralEvent {
	
	@Override
	public void onCallGeneral(FlagPack.FlagModifiers values, @Nullable LootDrop lootDrop, @Nullable IDrop iDrop, FlagTrigger trigger) {
		if(lootDrop==null) return;

		Location origin = lootDrop.getLocation();
		String skill = values.getString();
		Entity caster = (lootDrop.p!=null) ? lootDrop.p : lootDrop.sourceEntity;
		ArrayList<Entity> targets = new ArrayList<>();
		targets.add(caster);
		ArrayList<Location> locationTargets = new ArrayList<>();
		locationTargets.add(origin);

		MythicMobs.inst().getAPIHelper().castSkill(caster, skill, caster, origin, targets, locationTargets, 1.0f);
	}
	
}
