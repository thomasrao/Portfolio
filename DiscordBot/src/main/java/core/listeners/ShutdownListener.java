package core.listeners;

import core.server.ServerManager;
import core.server.channel.MessageChannelInfo;
import core.server.guild.GuildInfo;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tools.factories.ShitpostFactory;

/**
 * The event handler for shutting the bot down.
 *
 * @author Tom
 */
public class ShutdownListener extends ListenerAdapter {
    @Override
    public void onShutdown(ShutdownEvent event) {
        System.out.println("Saving...");
        ServerManager.getInstance().save();
        ShitpostFactory.getInstance().save();

        for (GuildInfo guild : ServerManager.getInstance().getGuilds()) {
            for (MessageChannelInfo channel : guild.getMessageChannels()) {
                channel.getGameManager().delete();
            }

            if (guild.getMusicManager() != null) {
                guild.getMusicManager().clear();
                guild.getMusicManager().disconnect();
            }
        }
        // TODO: remove this when shudownNow() is fixed.
        System.exit(0);
    }
}
