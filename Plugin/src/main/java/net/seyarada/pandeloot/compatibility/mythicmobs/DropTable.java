package net.seyarada.pandeloot.compatibility.mythicmobs;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.mobs.GenericCaster;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.ItemDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.drops.containers.IContainer;
import net.seyarada.pandeloot.flags.FlagPack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class DropTable implements IContainer {

    String id;
    FlagPack pack;
    public ArrayList<IDrop> dropList = new ArrayList<>();

    public DropTable(String id, FlagPack pack) {
        this.id = id;
        this.pack = pack;
    }

    @Override
    public ConfigurationSection getConfig() {
        return null;
    }

    @Override
    public List<IDrop> getDropList(LootDrop lootDrop) {
        Optional<io.lumine.xikage.mythicmobs.drops.DropTable> maybeDrops = MythicMobs.inst().getDropManager().getDropTable(id);
        final DropMetadata meta = new DropMetadata(new GenericCaster(BukkitAdapter.adapt(lootDrop.p)), BukkitAdapter.adapt(lootDrop.p));

        if(maybeDrops.isPresent())	{
            final io.lumine.xikage.mythicmobs.drops.DropTable dt = maybeDrops.get();

            if(dt.hasDrops()) {
                LootBag loot = dt.generate(meta);
                Collection<Drop> drops = loot.getDrops();
                for(Drop type : drops) {
                    if(type instanceof IItemDrop iDrop) {
                        FlagPack itemFlags = FlagPack.fromCompact(type.getLine());
                        itemFlags.merge(pack);
                        dropList.add(new ItemDrop(BukkitAdapter.adapt(iDrop.getDrop(meta)), itemFlags));
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
