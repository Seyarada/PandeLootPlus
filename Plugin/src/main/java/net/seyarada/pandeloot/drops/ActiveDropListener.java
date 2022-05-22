package net.seyarada.pandeloot.drops;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.PandeLoot;
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

    public void checkForLandings(Entity i, FlagPack pack){
        AtomicBoolean hasLanded = new AtomicBoolean(false);

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {

            if(!i.isValid()) Bukkit.getScheduler().cancelTask(id);

            if(i.isOnGround() && !hasLanded.get()) {
                ActiveDrop activeDrop = ActiveDrop.get(i);
                hasLanded.set(true);
                pack.trigger(FlagTrigger.onland, i, activeDrop.lootDrop, activeDrop.iDrop);
            } else if(!i.isOnGround()) {
                hasLanded.set(false);
            }

        }, 0, 3);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if( !(e.getEntity() instanceof Player player) ) return;

        Item i = e.getItem();
        ActiveDrop activeDrop = ActiveDrop.get(i);
        if(activeDrop==null) return;

        if(!activeDrop.canBePickedUp) {
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
            activeDrop.triggerRollBag(FlagTrigger.onspawn);
            activeDrop.stopLootBagRunnable();
            ItemUtils.removeData(i.getItemStack(), Constants.LOCK_LOOTBAG);
        }

        FlagPack pack = activeDrop.flags;
        if(pack==null) return;
        if(!pack.flags.containsKey(FlagTrigger.onpickup)) return;

        for(Map.Entry<IFlag, FlagPack.FlagModifiers> flag : pack.flags.get(FlagTrigger.onpickup).entrySet()) {
            IFlag flagClass = flag.getKey();
            FlagPack.FlagModifiers flagData = flag.getValue();

            if(flagClass instanceof IServerEvent)
                ((IServerEvent)flagClass).onCallCancellableEvent((Player) e.getEntity(), i, flagData, FlagTrigger.onpickup, e);
        }

        if(!i.isValid()) i = null;
        pack.trigger(FlagTrigger.onpickup, i, activeDrop.lootDrop, activeDrop.iDrop);
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
        ActiveDrop aE = ActiveDrop.get(e.getEntity());
        ActiveDrop aT = ActiveDrop.get(e.getTarget());
        if(aE!=null || aT!=null) {
            e.setCancelled(true);
        }
    }

}
