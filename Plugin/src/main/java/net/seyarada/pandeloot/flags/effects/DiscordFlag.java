package net.seyarada.pandeloot.flags.effects;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.drops.ItemDropMeta;
import net.seyarada.pandeloot.flags.FlagEffect;
import net.seyarada.pandeloot.flags.types.IGeneralEvent;

import java.awt.*;
import java.util.logging.Level;

@FlagEffect(id="discord", description="Link for DiscordSRV")
public class DiscordFlag implements IGeneralEvent {

	static final String URL = "https://crafatar.com/avatars/";

	@Override
	public void onCallGeneral(ItemDropMeta meta) {
		String msg = meta.getString();

		boolean isEmbed = meta.getBooleanOrDefault("embed", true);

		String title = meta.getString("title");
		String color = meta.getString("color");
		String thumbnail = meta.getString("thumbnail");
		String author = meta.getString("author");
		String footer = meta.getString("footer");
		String image = meta.getString("image");
		boolean useAvatar = meta.getBoolean("avatar");
		long channel = meta.getLong("channel");

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

			if(useAvatar && meta.lootDrop()!=null && meta.lootDrop().p!=null) builder.setThumbnail(URL+meta.lootDrop().p.getUniqueId());
			if(thumbnail!=null) builder.setThumbnail(thumbnail);

			builder.setDescription(msg);
			MessageEmbed embed = builder.build();
			discordChannel.sendMessageEmbeds(embed).queue();
		}
	}

}
