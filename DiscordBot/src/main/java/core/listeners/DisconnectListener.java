package core.listeners;

import core.server.ServerManager;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tools.factories.ShitpostFactory;
import tools.logging.Log;

/**
 * The event handler for bot disconnections.
 *
 * @author Tom
 */
public class DisconnectListener extends ListenerAdapter {
    @Override
    public void onDisconnect(DisconnectEvent event) {
        Log.getInstance().warn("Disconnection occurred.", "Disconnection");
        ServerManager.getInstance().save();
        ShitpostFactory.getInstance().save();
    }
}
