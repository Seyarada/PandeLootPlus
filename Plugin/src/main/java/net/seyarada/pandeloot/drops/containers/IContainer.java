package net.seyarada.pandeloot.drops.containers;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public interface IContainer extends IDrop {

    ConfigurationSection getConfig();

    List<IDrop> getDropList(LootDrop lootDrop);

    @Override
    default void run(LootDrop lootDrop) {
        Logger.userWarning("Impossible container drop from "+lootDrop+": "+getConfig().getName());
    }

    default void load() {

    }

}
