package net.seyarada.pandeloot.flags.effects;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.seyarada.pandeloot.drops.DropMeta;
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

@FlagEffect(id="skill", description="Casts a MythicMobs skill")
public class MythicSkillFlag implements IGeneralEvent {
	
	@Override
	public void onCallGeneral(DropMeta meta) {
		if(meta.lootDrop()==null) return;

		Location origin = meta.lootDrop().getLocation();
		String skill = meta.getString();
		Entity caster = (meta.lootDrop().p!=null) ? meta.lootDrop().p : meta.lootDrop().sourceEntity;
		ArrayList<Entity> targets = new ArrayList<>();
		targets.add(caster);
		ArrayList<Location> locationTargets = new ArrayList<>();
		locationTargets.add(origin);

		MythicBukkit.inst().getAPIHelper().castSkill(caster, skill, caster, origin, targets, locationTargets, 1.0f);
	}
	
}
