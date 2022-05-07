package net.seyarada.pandeloot.loot.providers.item;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.seyarada.pandeloot.api.ItemProvider;
import net.seyarada.pandeloot.compatibility.mmoitems.MIGeneratorCompatibility;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.TypeFlag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMOItemProvider implements ItemProvider {

    @Override
    public ItemStack getItem(String item, FlagPack pack, Player player, LootDrop drop) {
        return MIGeneratorCompatibility.getItem(item, pack, player, drop);
    }

    @Override
    public boolean isPresent(String item, FlagPack pack, Player player, LootDrop drop) {
        FlagPack.FlagModifiers miData = pack.getFlag(TypeFlag.class);
        Type type = MMOItems.plugin.getTypes().getOrThrow(miData.getString().toUpperCase().replace("-", "_"));
        return MMOItems.plugin.getTemplates().hasTemplate(type, item);
    }


}
