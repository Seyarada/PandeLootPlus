package net.seyarada.pandeloot.flags.effects;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.types.IEntityEvent;
import net.seyarada.pandeloot.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FlagEffect(id="explode", description="Applies an explosion-like velocity to the item")
public class ExplodeFlag implements IEntityEvent {

	@Override
	public void onCallEntity(Entity item, ItemDropMeta meta) {
		if(!meta.getBoolean()) return;

		String explosionType = meta.getOrDefault("type", "spread");

		switch (explosionType) {
			case "radial" -> doRadialDrop(item, meta.values(), meta.lootDrop());
			case "circular" -> doCircularDrop(item, meta.values());
			default -> doSpreadDrop(item, meta.values());
		}

	}


	void doSpreadDrop(Entity item, FlagPack.FlagModifiers meta) {
		final double offset = meta.getDoubleOrDefault("offset", 0.2);
		final double height = meta.getDoubleOrDefault("height", 0.6);
		Vector velocity = MathUtils.getVelocity(offset, height);

		item.setVelocity(velocity);
	}

	void doCircularDrop(Entity item, FlagPack.FlagModifiers meta) {
		double radius = meta.getDoubleOrDefault("radius", 3);

		Random rand = ThreadLocalRandom.current();
		double angle = rand.nextFloat() * 2 * Math.PI;
		double x = radius * Math.cos(angle);
		double z = radius * Math.sin(angle);

		item.setVelocity(MathUtils.calculateVelocity(item.getLocation().toVector(), item.getLocation().toVector().add(new Vector(x, 0, z)), 0.115, 3));
	}

	void doRadialDrop(Entity item, FlagPack.FlagModifiers meta, LootDrop drop) {
		double radius = meta.getDoubleOrDefault("radius", 3);

		int numberOfTotalDrops = 1;
		int numberOfThisDrop = 1;

		if(drop!=null) {
			numberOfTotalDrops = drop.size();
			numberOfThisDrop = Integer.parseInt(drop.data.getOrDefault("radialNumber", "1"));
			drop.data.put("radialNumber", String.valueOf(numberOfThisDrop+1));
		}

		if(numberOfTotalDrops==1) {
			doSpreadDrop(item, meta);
			return;
		}

		double angle = 2 * Math.PI / numberOfTotalDrops;
		double cos = Math.cos(angle * numberOfThisDrop);
		double sin = Math.sin(angle * numberOfThisDrop);
		double iX = item.getLocation().getX() + radius * cos;
		double iZ = item.getLocation().getZ() + radius * sin;

		Location loc = item.getLocation().clone();
		loc.setX(iX);
		loc.setZ(iZ);

		item.setVelocity(MathUtils.calculateVelocity(item.getLocation().toVector(), loc.toVector(), 0.115, 3));
	}

}
