package core.listeners;

import core.server.ServerManager;
import core.server.guild.GuildInfo;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for deleting voice channels.
 *
 * @author Tom
 */
public class VoiceChannelDeleteListener extends ListenerAdapter {
    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
        guild.removeVoiceChannel(event.getChannel().getIdLong());
    }
}
