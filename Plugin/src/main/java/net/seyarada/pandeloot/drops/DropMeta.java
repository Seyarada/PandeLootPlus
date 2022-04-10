package net.seyarada.pandeloot.drops;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public record DropMeta(Entity source, Player target, LootDrop drop) {
}
