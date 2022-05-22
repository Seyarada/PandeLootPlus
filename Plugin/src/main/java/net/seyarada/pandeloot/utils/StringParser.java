package net.seyarada.pandeloot.utils;

import com.google.common.collect.Sets;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.core.skills.placeholders.parsers.PlaceholderStringImpl;
import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.drops.LootDrop;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class StringParser {

    static final StrSubstitutor SUBSTITUTOR;

    static {
        final HashMap<String, String> placeholderValues = new HashMap<>();

        placeholderValues.put("rb", "[");
        placeholderValues.put("lb", "]");
        placeholderValues.put("rs", "<");
        placeholderValues.put("ls", ">");
        placeholderValues.put("sc", ";");

        SUBSTITUTOR = new StrSubstitutor(placeholderValues, "%", "%");
    }

    public static String parse(String text, ItemDropMeta meta) {
        if(text==null) return null;

        try {
            return parseText(text, meta.lootDrop());
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }
    }

    public static double parseAndMath(String text, LootDrop drop) {
        if(text==null) return 0;

        text = parseText(text, drop);

        try {
            return MathUtils.eval(text);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String parseText(String text, LootDrop drop) {
        text = SUBSTITUTOR.replace(text);
        text = ChatUtils.translateHexCodes(text);

        Player player = (drop==null) ? null : drop.p;

        if(drop!=null)
            text = drop.sub.replace(text);

        if(PandeLoot.mythicEnabled && drop!=null && text.contains("<") && text.contains(">")) {
            AbstractEntity mob = BukkitAdapter.adapt(drop.sourceEntity);
            Optional<ActiveMob> optionalMob = MythicBukkit.inst().getMobManager().getActiveMob(drop.sourceEntity.getUniqueId());
            if(optionalMob.isPresent()) {
                ActiveMob mobCaster = optionalMob.get();
                AbstractEntity playerTarget = BukkitAdapter.adapt(player);
                HashSet<AbstractEntity> targets = Sets.newHashSet();
                targets.add(playerTarget);
                SkillMetadata meta = new SkillMetadataImpl(SkillTriggers.API, mobCaster, mob, mob.getLocation(), targets, null, 1);
                text = new PlaceholderStringImpl(text).get(meta, playerTarget);
            }
        }

        if(player!=null && PandeLoot.papiEnabled)
            text = PlaceholderAPI.setPlaceholders(player, text);

        if(text.chars().filter(ch -> ch == '%').count() >= 2)
            return null;

        return text;
    }

}
