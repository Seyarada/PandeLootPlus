package net.seyarada.pandeloot.compatibility.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.item.template.explorer.ClassFilter;
import net.Indyuce.mmoitems.api.item.template.explorer.IDFilter;
import net.Indyuce.mmoitems.api.item.template.explorer.TemplateExplorer;
import net.Indyuce.mmoitems.api.item.template.explorer.TypeFilter;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.TypeFlag;
import net.seyarada.pandeloot.utils.StringParser;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Random;

public class MIGeneratorCompatibility {

    private static final Random random = new Random();

    public static ItemStack getItem(String item, FlagPack pack, Player player, LootDrop drop) {
        FlagPack.FlagModifiers miData = pack.getFlag(TypeFlag.class);
        String type = miData.getString();

        if(type==null || type.isBlank())
            return MMOItemsCompatibility.getItem(item, pack, player);

        RPGPlayer rpgPlayer = player != null ? PlayerData.get(player).getRPG() : null;

        int itemLevel;
        ItemTier itemTier = null;
        TemplateExplorer builder = new TemplateExplorer();

        if(miData.containsKey("level")) {
            itemLevel = (int) StringParser.parseAndMath(miData.getString("level"), drop);
        } else if(miData.containsKey("matchlevel") && rpgPlayer!=null) {
            itemLevel = MMOItems.plugin.getTemplates().rollLevel(rpgPlayer.getLevel());
        } else itemLevel = (1 + random.nextInt(100));

        if(miData.containsKey("tier")) {
            String tier = miData.getString("tier").toUpperCase().replace("-", "_");
            itemTier = MMOItems.plugin.getTiers().getOrThrow(tier);
        };

        if (miData.containsKey("matchclass") && rpgPlayer!=null)
            builder.applyFilter(new ClassFilter(rpgPlayer));

        if (miData.containsKey("class")) {
            String playerClass = miData.getString("class").replace("-", " ").replace("_", " ");
            builder.applyFilter(new ClassFilter(playerClass));
        }

        builder.applyFilter(new TypeFilter(Type.get(type)));
        builder.applyFilter(new IDFilter(item));

        Optional<MMOItemTemplate> optional = builder.rollLoot();
        if(optional.isEmpty()) {
            return new ItemStack(Material.STONE, 1);
        }

        ItemStack iS = optional.get().newBuilder(itemLevel, itemTier).build().newBuilder().build();
        Validate.isTrue((iS != null && iS.getType() != Material.AIR), "Could not generate item with ID '" + optional.get().getId() + "'");
        return iS;
    }

}
