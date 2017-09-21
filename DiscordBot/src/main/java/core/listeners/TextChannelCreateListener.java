package core.listeners;

import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.Game;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for creating text channels.
 *
 * @author Tom
 */
public class TextChannelCreateListener extends ListenerAdapter {
    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
        guild.addChannel(event.getChannel());

        for (ChannelInfo channel : guild.getMessageChannels()) {
            Game game = channel.getGameManager().getGame();
            if (game != null)
                game.onTextChannelCreate(event.getChannel());
        }
    }
}
