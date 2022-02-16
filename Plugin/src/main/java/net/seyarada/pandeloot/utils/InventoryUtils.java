package net.seyarada.pandeloot.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static void fillArea(int rowA, int columnA, int rowB, int columnB, Inventory inv, ItemStack iS) {
        for (int i = 0; i < rowB-rowA+1; ++i) {
            for (int j = 0; j < columnB-columnA+1; ++j) {
                inv.setItem(getSlot(rowA+i, columnA+j), iS);
            }
        }
    }

    static int getSlot(int row, int column) {
        return (row-1)*9+column-1;
    }

}
