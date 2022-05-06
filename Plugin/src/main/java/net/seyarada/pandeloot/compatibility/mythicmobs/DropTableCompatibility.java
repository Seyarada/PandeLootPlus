package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.mythic.api.adapters.AbstractItemStack;
import io.lumine.mythic.api.drops.DropMetadata;
import io.lumine.mythic.api.drops.IItemDrop;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.drops.Drop;
import io.lumine.mythic.core.drops.DropMetadataImpl;
import io.lumine.mythic.core.drops.DropTable;
import io.lumine.mythic.core.drops.LootBag;
import io.lumine.mythic.core.drops.droppables.CustomDrop;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.flags.FlagManager;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.AmountFlag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DropTableCompatibility implements IContainer {

    String id;
    FlagPack pack;
    public ArrayList<IDrop> dropList = new ArrayList<>();

    public DropTableCompatibility(String id, FlagPack pack) {
        this.id = id;
        this.pack = pack;
    }

    @Override
    public ConfigurationSection getConfig() {
        return null;
    }

    @Override
    public List<IDrop> getDropList(LootDrop lootDrop) {
        Optional<DropTable> maybeDrops = MythicBukkit.inst().getDropManager().getDropTable(id);
        final DropMetadata meta = new DropMetadataImpl(new GenericCaster(BukkitAdapter.adapt(lootDrop.p)), BukkitAdapter.adapt(lootDrop.p));

        if(maybeDrops.isPresent())	{
            final DropTable dt = maybeDrops.get();

            if(dt.hasDrops()) {
                LootBag loot = dt.generate(meta);
                Collection<Drop> drops = loot.getDrops();


                for(Drop type : drops) {
                    FlagPack itemFlags = FlagPack.fromCompact(type.getLine());
                    itemFlags.merge(pack);
                    int amount = 1;
                    AmountFlag amountFlag = (AmountFlag) FlagManager.getFromID("amount");
                    if(itemFlags.hasFlag(amountFlag))
                        amount = AmountFlag.getValueFromRanged(itemFlags.getFlag(amountFlag).getString());

                    if(type instanceof IItemDrop iDrop) {
                        dropList.add(new ItemDrop(BukkitAdapter.adapt(iDrop.getDrop(meta, amount)), itemFlags));
                    }

                    else if (type instanceof CustomDrop customDrop) {
                        if(customDrop.getDrop().isPresent() && customDrop.getDrop().get() instanceof IItemDrop itemDrop) {
                            dropList.add(new ItemDrop(BukkitAdapter.adapt(itemDrop.getDrop(meta, amount)), itemFlags));
                        }
                    }
                }
            }
        }
        return dropList;
    }

    @Override
    public FlagPack getFlagPack() {
        return new FlagPack();
    }

    @Override
    public ItemStack getItemStack() {
        Logger.log(Level.WARNING, "Impossible getItemStack from DropTable: "+id);
        return null;
    }

}
