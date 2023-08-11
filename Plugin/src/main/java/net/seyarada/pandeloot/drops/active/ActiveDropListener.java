package net.seyarada.pandeloot.drops.active;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.active.ItemActive;
import net.seyarada.pandeloot.drops.containers.LootBag;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IFlag;
import net.seyarada.pandeloot.flags.types.IServerEvent;
import net.seyarada.pandeloot.nms.NMSManager;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ActiveDropListener implements Listener {

    private int id;

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if( !(e.getEntity() instanceof Player player) ) return;

        Item i = e.getItem();
        BaseActive activeDrop = ItemActive.get(i);
        if(!(activeDrop instanceof LootBagActive lootBag)) return;

        if(!lootBag.canBePickedUp) {
            e.setCancelled(true);
            return;
        }

        if(NMSManager.isHiddenFor(i.getEntityId(), player.getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        PersistentDataContainer data = i.getItemStack().getItemMeta().getPersistentDataContainer();
        boolean isLocked = data.has(Constants.LOCK_LOOTBAG, PersistentDataType.STRING);

        if(isLocked) {
            lootBag.triggerRollBag(FlagTrigger.onspawn);
            lootBag.stopTask(BaseActive.BukkitTask.LOOTBAG_ROLLER);
            ItemUtils.removeData(i.getItemStack(), Constants.LOCK_LOOTBAG);
        }

        FlagPack pack = lootBag.flags;
        if(pack==null) return;
        if(!pack.flags.containsKey(FlagTrigger.onpickup)) return;

        for(Map.Entry<IFlag, FlagPack.FlagModifiers> flag : pack.flags.get(FlagTrigger.onpickup).entrySet()) {
            IFlag flagClass = flag.getKey();
            FlagPack.FlagModifiers flagData = flag.getValue();

            if(flagClass instanceof IServerEvent)
                ((IServerEvent)flagClass).onCallCancellableEvent((Player) e.getEntity(), i, flagData, FlagTrigger.onpickup, e);
        }

        if(!i.isValid()) i = null;
        pack.trigger(FlagTrigger.onpickup, i, lootBag.lootDrop, lootBag.iDrop);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) return;

        if (e.getHand() == EquipmentSlot.HAND) {
            LootBag.openDroppedLootBag(e);
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent e) {
        ItemActive aE = ItemActive.get(e.getEntity());
        ItemActive aT = ItemActive.get(e.getTarget());
        if(aE!=null || aT!=null) {
            e.setCancelled(true);
        }
    }

}
