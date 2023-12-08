package net.seyarada.pandeloot.drops.containers;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.drops.ActiveDrop;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class LootBag extends LootTable {

    public LootBag(ConfigurationSection config) {
        super(config);
    }

    public static void openDroppedLootBag(PlayerInteractEvent e) {
        ItemStack iS;
        PersistentDataContainer data;

        // Lootbag on player
        iS = e.getPlayer().getEquipment().getItemInMainHand();
        if(iS.getType()==Material.AIR) iS = e.getPlayer().getEquipment().getItemInOffHand();
        if(iS.hasItemMeta()) {
            data = iS.getItemMeta().getPersistentDataContainer();

            if(data.has(Constants.LOOTBAG_KEY, PersistentDataType.STRING)) {
                String id = data.get(Constants.LOOTBAG_KEY, PersistentDataType.STRING);

                if(data.has(Constants.KEY, PersistentDataType.STRING)) {
                    FlagPack flagPack = FlagPack.fromCompact(data.get(Constants.KEY, PersistentDataType.STRING));
                    if(flagPack.passesConditions(FlagTrigger.onopen, null, e.getPlayer()))
                        flagPack.trigger(FlagTrigger.onopen, null, e.getPlayer());
                    else return;
                }

                iS.setAmount(iS.getAmount()-1);
                new LootDrop(ContainerManager.get(id), e.getPlayer(), e.getPlayer().getLocation())
                        .build()
                        .drop();
                return;
            }
        }


        // Lootbag on ground
        if(e.getClickedBlock()==null) return;
        Location loc = e.getClickedBlock().getLocation();
        for(Entity i : loc.getWorld().getNearbyEntities(loc, 1.5, 1.5 ,1.5)) {
            if(!(i instanceof Item item)) continue;
            iS = item.getItemStack();
            data = iS.getItemMeta().getPersistentDataContainer();

            ActiveDrop aDrop = ActiveDrop.get(item);

            boolean isLocked = data.has(Constants.LOCK_LOOTBAG, PersistentDataType.STRING);
            String id = null;

            if(isLocked) {
                aDrop.triggerRollBag(FlagTrigger.onspawn);
                aDrop.stopLootBagRunnable();
                ItemUtils.removeData(iS, Constants.LOCK_LOOTBAG);
            }

            if(!isLocked) {
                if(!data.has(Constants.LOOTBAG_KEY, PersistentDataType.STRING))
                    continue;
                id = data.get(Constants.LOOTBAG_KEY, PersistentDataType.STRING);
            }

            if(aDrop.amountOpened>=iS.getAmount()) return;

            if(data.has(Constants.KEY, PersistentDataType.STRING)) {
                FlagPack flagPack = FlagPack.fromCompact(data.get(Constants.KEY, PersistentDataType.STRING));
                if(flagPack.passesConditions(FlagTrigger.onopen, item, e.getPlayer()))
                    flagPack.trigger(FlagTrigger.onopen, item, e.getPlayer());
                else return;
            }

            aDrop.amountOpened++;

            playArm(e.getPlayer());

            if(isLocked || id==null) {
                return;
            }

            iS.setAmount(iS.getAmount()-1);
            if(iS.getAmount()<=0) item.remove();

            new LootDrop(ContainerManager.get(id), e.getPlayer(), item.getLocation())
                    .build()
                    .drop();
            return;
        }
    }

    public IDrop getDrop(FlagPack pack) {
        ItemStack iS = ItemUtils.getItemFromSection(config);
        FlagPack droppedFlags = new FlagPack();
        droppedFlags.merge(pack);
        droppedFlags.flags.remove(FlagTrigger.onspawn);
        ItemUtils.setFlags(iS, droppedFlags);
        ItemUtils.writeData(iS, Constants.LOOTBAG_KEY, config.getName());

        ItemDrop itemDrop = new ItemDrop(iS, pack);
        itemDrop.data.put(Constants.LOOTBAG_KEY, config.getName());
        return itemDrop;
    }

    @Override
    public ItemStack getItemStack() {
        return ItemUtils.getItemFromSection(config);
    }

    static void playArm(Player player) {
        // Prevent causing swing events for items from other plugins
        Material mainHand = player.getInventory().getItemInMainHand().getType();
        Material offHand = player.getInventory().getItemInOffHand().getType();
        if(mainHand==Material.AIR && offHand==Material.AIR)
            player.swingMainHand();

    }

}
