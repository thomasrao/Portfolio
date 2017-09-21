package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tools.factories.ShitpostFactory;

/**
 * The event handler for when the bot is ready to listen.
 *
 * @author Tom
 */
public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent e) {
        ServerManager.getInstance().load(e.getJDA().getGuilds());
        ShitpostFactory.getInstance().load();
    }
}
