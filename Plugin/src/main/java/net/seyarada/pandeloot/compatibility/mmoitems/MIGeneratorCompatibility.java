package net.seyarada.pandeloot.compatibility.mmoitems;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.build.MMOItemBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.api.player.RPGPlayer;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.effects.TypeFlag;
import net.seyarada.pandeloot.utils.StringParser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MIGeneratorCompatibility {

    public static ItemStack getItem(String itemStr, FlagPack pack, Player player, LootDrop drop) {
        FlagPack.FlagModifiers miData = pack.getFlag(TypeFlag.class);
        String typeStr = miData.getString();

        RPGPlayer rpgPlayer = player != null ? PlayerData.get(player).getRPG() : null;

        Type type = MMOItems.plugin.getTypes().getOrThrow(typeStr.toUpperCase().replace("-", "_"));

        MMOItemTemplate template = MMOItems.plugin.getTemplates().getTemplateOrThrow(type, itemStr.toUpperCase().replace("-", "_"));

        int itemLevel = 0;
        if(miData.containsKey("level")) {
            itemLevel = (int) StringParser.parseAndMath(miData.getString("level"), drop);
        } else if(miData.containsKey("matchlevel") && rpgPlayer!=null) {
            itemLevel = MMOItems.plugin.getTemplates().rollLevel(rpgPlayer.getLevel());
        } else if(template.hasOption(MMOItemTemplate.TemplateOption.LEVEL_ITEM) && rpgPlayer!=null)  {
            MMOItems.plugin.getTemplates().rollLevel(rpgPlayer.getLevel());
        }


        ItemTier itemTier = null;
        if(miData.containsKey("tier")) {
            String tier = miData.getString("tier").toUpperCase().replace("-", "_");
            itemTier = MMOItems.plugin.getTiers().getOrThrow(tier);
        } else if(template.hasOption(MMOItemTemplate.TemplateOption.TIERED)) {
            itemTier = MMOItems.plugin.getTemplates().rollTier();
        }

        MMOItemBuilder builder = new MMOItemBuilder(template, itemLevel, itemTier);

        MMOItem mmoitem = builder.build();
        return mmoitem.newBuilder().build();

















    /*
    // There's going to be someone complaining about some of the settings here missing in the new version I swear


        if(type==null || type.isBlank())
            return MMOItemsCompatibility.getItem(mmoitem.newBuilder().build(), pack, player);

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
        }

        if (miData.containsKey("matchclass") && rpgPlayer!=null)
            builder.applyFilter(new ClassFilter(rpgPlayer));

        if (miData.containsKey("class")) {
            String playerClass = miData.getString("class").replace("-", " ").replace("_", " ");
            builder.applyFilter(new ClassFilter(playerClass));
        }

        builder.applyFilter(new TypeFilter(Type.get(type)));
        builder.applyFilter(new IDFilter(mmoitem.newBuilder().build()));

        Optional<MMOItemTemplate> optional = builder.rollLoot();
        if(optional.isEmpty()) {
            return new ItemStack(Material.STONE, 1);
        }

        ItemStack iS = optional.get().newBuilder(itemLevel, itemTier).build().newBuilder().build();
        Validate.isTrue((iS != null && iS.getType() != Material.AIR), "Could not generate item with ID '" + optional.get().getId() + "'");
        return iS;

     */
    }

}
