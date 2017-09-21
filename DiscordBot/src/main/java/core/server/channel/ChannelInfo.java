package core.server.channel;

import games.GameManager;
import net.dv8tion.jda.core.entities.Channel;

/**
 * Additional details kept regarding each channel.
 *
 * @author Tom
 */
public abstract class ChannelInfo<T extends Channel> {
    protected T channel;
    private GameManager gameManager;


    public ChannelInfo(T channel) {
        this.channel = channel;
        this.gameManager = new GameManager(channel.getGuild().getIdLong(), channel.getIdLong());
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    public T getChannel() {
        return channel;
    }

    public long getId() {
        return channel.getIdLong();
    }
}
