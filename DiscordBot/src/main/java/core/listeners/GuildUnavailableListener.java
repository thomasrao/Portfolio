package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for guilds that cannot
 *  be connected upon the bot's initialization.
 *
 * @author Tom
 */
public class GuildUnavailableListener extends ListenerAdapter {
    @Override
    public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event) {
        ServerManager.getInstance().get(event.getGuildIdLong()).save();
        ServerManager.getInstance().remove(event.getGuildIdLong());
    }
}
