package core.listeners;

import core.server.ServerManager;
import core.server.guild.GuildInfo;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for creating voice channels.
 *
 * @author Tom
 */
public class VoiceChannelCreateListener extends ListenerAdapter {
    @Override
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
        guild.addChannel(event.getChannel());
    }
}
