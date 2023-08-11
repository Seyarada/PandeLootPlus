package net.seyarada.pandeloot.drops.active;

import net.seyarada.pandeloot.Constants;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LootBagActive extends ItemActive {

    FlagPack rollBagPack;
    boolean triggerOnSpawn;
    public int amountOpened = 0;

    public LootBagActive(IDrop drop, Entity e, Player p, FlagPack pack, LootDrop lootDrop) {
        super(drop, e, p, pack, lootDrop);
    }

    public void triggerRollBag(FlagTrigger trigger) {
        if(triggerOnSpawn) rollBagPack.trigger(trigger, e, lootDrop, iDrop);
    }
    public void startLootBagRollRunnable(IContainer bag, LootDrop drop, FlagPack flags, boolean triggerOnSpawn, int frequency) {
        this.triggerOnSpawn = triggerOnSpawn;
        Item i = (Item)e;
        FlagPack droppedFlags = new FlagPack();
        droppedFlags.merge(flags);
        droppedFlags.flags.remove(FlagTrigger.onspawn);
        List<IDrop> drops = bag.getDropList(drop);

        addTask(BukkitTask.LOOTBAG_ROLLER, Bukkit.getScheduler().scheduleSyncRepeatingTask(PandeLoot.inst, () -> {
            if(!e.isValid()) cancelTasks();

            IDrop iDrop = drops.get((int) (Math.random() * drops.size()));
            ItemStack iS = iDrop.getItemStack();
            ItemUtils.writeData(iS, Constants.LOCK_LOOTBAG, "true");
            FlagPack combinedPack = new FlagPack();
            rollBagPack = iDrop.getFlagPack();
            combinedPack.merge(rollBagPack);
            combinedPack.merge(droppedFlags);
            //ItemUtils.setFlags(iS, combinedPack);

            combinedPack.trigger(FlagTrigger.onroll, i, drop.p);

            i.setItemStack(iS);

        }, 0, frequency));
    }

}
