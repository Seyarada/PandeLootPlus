package net.seyarada.pandeloot.compatibility.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.droptable.item.MMOItemDropItem;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.TypeFlag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMOItemsCompatibility {

    public static ItemStack getItem(String item, FlagPack pack, Player player) {
        FlagPack.FlagModifiers values = pack.getFlag(TypeFlag.class);
        String typeFormat = values.getString().toUpperCase().replace("-", "_");
        Type type = MMOItems.plugin.getTypes().get(typeFormat);
        item = item.toUpperCase();

        Logger.log(values.getString());
        Logger.log(type);
        Logger.log(type.getName());
        Logger.log(type.getItem());
        Logger.log(item);

        MMOItemDropItem dropItem;
        if(values.getString("unidentified")!=null)
            dropItem = new MMOItemDropItem(type, item, 1.0D, values.getDouble("unidentified"), 1, 1);
        else
            dropItem = new MMOItemDropItem(type, item, 1.0D, 0d, 1, 1);

        Logger.log(dropItem);

        PlayerData playerData = player != null ? PlayerData.get(player) : null;
        Logger.log(playerData);
        Logger.log(dropItem.getId());

        System.err.println(MMOItems.plugin.getItem(type, item));

        return dropItem.getItem(playerData);
    }

}
