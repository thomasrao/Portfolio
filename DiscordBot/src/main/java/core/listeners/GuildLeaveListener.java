package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The event handler for guilds that
 *  kicked the bot out.
 *
 * @author Tom
 */
public class GuildLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        ServerManager.getInstance().remove(event.getGuild().getIdLong());
    }
}
