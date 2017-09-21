package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for available guilds
 *  to connect to upon bot initialization.
 *
 * @author Tom
 */
public class GuildAvailableListener extends ListenerAdapter {
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        ServerManager.getInstance().add(event.getGuild());
    }
}
