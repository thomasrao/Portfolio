package core.listeners;

import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.Game;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for deleting text channels.
 *
 * @author Tom
 */
public class TextChannelDeleteListener extends ListenerAdapter {
    @Override
    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        GuildInfo guild = ServerManager.getInstance().get(event.getGuild().getIdLong());
        guild.removeTextChannel(event.getChannel().getIdLong());

        for (ChannelInfo channel : guild.getMessageChannels()) {
            Game game = channel.getGameManager().getGame();
            if (game != null)
                game.onTextChannelDelete(event.getChannel());
        }
    }
}
