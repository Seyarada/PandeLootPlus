package net.seyarada.pandeloot.gui;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.config.Config;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.ContainerManager;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.utils.InventoryUtils;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContainersGUI implements Listener, InventoryHolder {

    Inventory inventory =  Bukkit.createInventory(this, 54, "Containers");
    Player player;

    IContainer viewing;

    public ContainersGUI(Player player) {
        Logger.log("Opening GUI for "+player);
        if(player==null) return;
        this.player = player;
        Logger.log("Creating main page");
        mainPage();
        Logger.log("Opening inventory "+inventory+" for "+player);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack iS = e.getCurrentItem();
        InventoryHolder inventoryHolder = p.getOpenInventory().getTopInventory().getHolder();
        if (inventoryHolder instanceof ContainersGUI gui) {
            e.setCancelled(true);
            if(iS==null || iS.getType()==Material.BLACK_STAINED_GLASS_PANE) return;

            if(gui.viewing==null) {
                String id = iS.getItemMeta().getPersistentDataContainer().get(Constants.KEY, PersistentDataType.STRING);
                gui.openContainer(id);
                return;
            }

            switch (e.getRawSlot()) {
                case 6 -> gui.rollTable();
                case 7 -> {
                    Config.reload();
                    gui.viewing = ContainerManager.get(gui.viewing.getConfig().getName());
                    gui.rollTable();
                }
                case 1 -> gui.mainPage();
            }

        }
    }

    void openContainer(String id) {
        viewing = ContainerManager.get(id);
        InventoryUtils.fillArea(2,1, 5,9, inventory, new ItemStack(Material.AIR));
        inventory.setItem(4, getItemForContainer(id));
        inventory.setItem(6, ItemUtils.createItem(Material.GOLD_NUGGET, ChatColor.YELLOW+"Roll Items"));
        inventory.setItem(7, ItemUtils.createItem(Material.END_PORTAL_FRAME, ChatColor.LIGHT_PURPLE+"Reload Plugin"));
        inventory.setItem(1, ItemUtils.createItem(Material.BARRIER, ChatColor.RED+"Back"));
        viewing.getDropList(null).forEach(d -> inventory.addItem(d.getItemStack()));
    }

    void rollTable() {
        InventoryUtils.fillArea(2,1, 5,9, inventory, new ItemStack(Material.AIR));
        LootDrop lootDrop = new LootDrop(Collections.singletonList(viewing), player, null);
        viewing.getDropList(lootDrop).forEach(d -> inventory.addItem(d.getItemStack()));
    }

    void mainPage() {
        viewing = null;
        InventoryUtils.fillArea(1,1, 1,9, inventory, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        InventoryUtils.fillArea(6,1, 6,9, inventory, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        InventoryUtils.fillArea(2,1, 5,9, inventory, new ItemStack(Material.AIR));
        ContainerManager.getLootTables().forEach(s -> inventory.addItem(getItemForContainer(s)));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    static ItemStack getItemForContainer(String id) {
        ItemStack iS = new ItemStack(Material.MAP);
        ItemMeta meta = iS.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW+id);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA+"LootTable");
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(Constants.KEY, PersistentDataType.STRING, id);
        iS.setItemMeta(meta);
        return iS;
    }

}
