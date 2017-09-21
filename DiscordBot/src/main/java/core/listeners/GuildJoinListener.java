package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for joining guilds
 *  when the bot is already connected.
 *
 * @author Tom
 */
public class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        ServerManager.getInstance().add(event.getGuild());
    }
}
