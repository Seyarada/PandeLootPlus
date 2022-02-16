package net.seyarada.pandeloot.flags.effects;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.IDrop;
import net.seyarada.pandeloot.drops.LootDrop;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.FlagPack;
import net.seyarada.pandeloot.flags.enums.FlagTrigger;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

import java.awt.*;
import java.util.logging.Level;

@FlagEffect(id="discord", description="Broadcast a message")
public class DiscordFlag implements IGeneralEvent {

	static final String URL = "https://crafatar.com/avatars/";

	@Override
	public void onCallGeneral(FlagPack.FlagModifiers values, LootDrop lootDrop, IDrop iDrop, FlagTrigger trigger) {
		String msg = (lootDrop!=null) ? lootDrop.parse(values.getString()) : values.getString();

		boolean isEmbed = values.getBooleanOrDefault("embed", true);

		String title = values.getString("title");
		String color = values.getString("color");
		String thumbnail = values.getString("thumbnail");
		String author = values.getString("author");
		String footer = values.getString("footer");
		String image = values.getString("image");
		boolean useAvatar = values.getBoolean("avatar");
		long channel = values.getLong("channel");

		TextChannel discordChannel = DiscordUtil.getJda().getTextChannelById(channel);
		if(discordChannel==null) {
			Logger.log(Level.WARNING, "Couldn't find discord channel by ID "+channel);
			return;
		}

		if(!isEmbed) {
			discordChannel.sendMessage(msg).queue();
		} else {
			EmbedBuilder builder = new EmbedBuilder();
			if(title!=null) builder.setTitle(title);
			if(color!=null) builder.setColor(Color.decode(color));
			if(author!=null) builder.setAuthor(author);
			if(footer!=null) builder.setFooter(footer);
			if(image!=null) builder.setImage(image);

			if(useAvatar && lootDrop!=null && lootDrop.p!=null) builder.setThumbnail(URL+lootDrop.p.getUniqueId());
			if(thumbnail!=null) builder.setThumbnail(thumbnail);

			builder.setDescription(msg);
			MessageEmbed embed = builder.build();
			discordChannel.sendMessageEmbeds(embed).queue();
		}
	}

}
