package games;

import core.server.ServerManager;
import core.server.guild.GuildInfo;
import games.blackjack.Blackjack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import tools.logging.Log;

/**
 * @author Tom
 */
public final class GameManager {
    private Game game;      // The game itself
    private long guildId;   // The guild that this manager is using
    private long channelId; // The channel that this manager is using


    public GameManager(long guildId, long channelId) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.game = null;
    }


    /**
     * Creates a game based on the type.
     * @param id the name given to this game.
     * @param type the type of the game.
     * @param ownerId the id of the owner of this game.
     */
    public void create(String id, GameType type, long ownerId) {
        GuildInfo guild = ServerManager.getInstance().get(guildId);
        if (guild != null) {
            TextChannel channel = guild.getTextChannel(channelId).getChannel();
            if (channel != null) {
                Member owner = guild.getMemberDetail(ownerId).getMember();
                if (owner != null) {
                    switch (type) {
                        case BLACKJACK:
                            game = new Blackjack(id, guild.getGuild(), channel, owner);
                            break;
                        default:
                            Log.getInstance().warn("Found a new game type unhandled: " + type.name(), "GameManager");
                            return;
                    }
                    game.add(ownerId);
                }
            }
        }
    }

    /**
     * Stops and deletes the game.
     */
    public void delete() {
        if (game != null) {
            game.stop();
            game = null;
        }
    }

    /**
     * Gets the game.
     * @return the game.
     */
    public Game getGame() {
        return game;
    }
}
