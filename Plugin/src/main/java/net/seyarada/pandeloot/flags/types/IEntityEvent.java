package net.seyarada.pandeloot.flags.types;

import net.seyarada.pandeloot.drops.ItemDropMeta;
import org.bukkit.entity.Entity;

public interface IEntityEvent extends IFlag {

	void onCallEntity(Entity entity, ItemDropMeta meta);

}
