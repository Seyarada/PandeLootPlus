package net.seyarada.pandeloot.nms;

import net.minecraft.network.protocol.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.Reference;
import java.util.List;

public interface NMSMethods {

    List<Entity> hologram(Location location, Player player, List<String> text, JavaPlugin plugin);

    void destroyEntity(int entityId, Player entity);

    void displayToast(Player player, String title, String frame, ItemStack icon);

    void injectPlayer(Player player);

    void removePlayer(Player player);

    void updateHologramPosition(double x, double y, double z, Entity hologram, Player player);

    void sendPacket(Player player, Object... packets);

}
