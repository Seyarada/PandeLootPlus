package net.seyarada.pandeloot.nms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface NMSMethods {

    List<Entity> hologram(int duration, Location location, Player player, List<String> text, JavaPlugin plugin);

    void destroyEntity(int toBeDestroyed, Entity entity);

    void displayToast(Player player, String title, String frame, ItemStack icon);

    void injectPlayer(Player player);

    void removePlayer(Player player);

    void updateHologramPosition(double x, double y, double z, Entity hologram, Player player);

    ItemStack getCustomTextureHead(ItemStack head, String value);

}
