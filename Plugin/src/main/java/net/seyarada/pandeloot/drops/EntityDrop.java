package net.seyarada.pandeloot.drops;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public final class EntityDrop implements IDrop {

    public Entity entity;
    public String entityID;
    public EntityDropType type;
    public FlagPack pack;

    public EntityDrop(Entity entity, FlagPack pack) {
        this.entity = entity;
        this.pack = pack;
    }

    public EntityDrop(String entityID, EntityDropType type, FlagPack pack) {
        this.entityID = entityID;
        this.type = type;
        this.pack = pack;
    }

    @Override
    public void run(LootDrop lootDrop) {
        if(entity!=null) new ActiveDrop(this, entity, lootDrop.p, pack, lootDrop);
        if(type==null) return;
        switch (type) {
            case VANILLA -> {
                Entity e = lootDrop.getLocation().getWorld().spawnEntity(lootDrop.getLocation(), EntityType.valueOf(entityID));
                new ActiveDrop(this, e, lootDrop.p, pack, lootDrop);
            }
            case MYTHICMOBS -> {
                Entity e = MythicBukkit.inst().getMobManager().spawnMob(entityID, lootDrop.getLocation()).getEntity().getBukkitEntity();
                new ActiveDrop(this, e, lootDrop.p, pack, lootDrop);
            }
        }
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack iS = new ItemStack(Material.SKELETON_SPAWN_EGG);
        iS.getItemMeta().setLore(Collections.singletonList(ChatColor.YELLOW + entityID));
        return iS;
    }

    @Override
    public FlagPack getFlagPack() {
        return pack;
    }

    public enum EntityDropType {
        VANILLA,
        MYTHICMOBS
    }

}
