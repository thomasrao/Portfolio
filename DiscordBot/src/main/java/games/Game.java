package games;

import core.server.ServerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.Date;

/**
 * @author Tom
 */
public abstract class Game {
    protected final String gameId;     // The game's id, used as a name as well.
    protected final GameType type;     // The type of this game.
    protected GameState state;         // The current state of the game.
    protected Guild guild;             // The guild that this game is using.
    protected MessageChannel channel;  // The channel that this game is using.
    private Member owner;              // The owner/creator of this game.
    private final Date creation;       // The date when the game was created.
    private Date start;                // The date when the game started.
    private Date end;                  // The date when the game ended.


    protected Game(String id, GameType type, Guild guild, MessageChannel channel, Member owner) {
        this.gameId = id;
        this.type = type;
        this.guild = guild;
        this.channel = channel;
        this.owner = owner;
        state = GameState.PREREGISTRATION;
        creation = new Date();
        start = null;
        end = null;
    }


    /**
     * Adds a participant to the game.
     * @param memberId the new participant.
     * @return true if the new participant joins the game, false otherwise.
     */
    public final boolean add(long memberId) {
        if (!isPlaying(memberId) && getPlayerCount() < type.getMaximumPlayerCount()) {
            Member member = ServerManager.getInstance().get(guild.getIdLong()).getMemberDetail(memberId).getMember();
            if (member != null) {
                onParticipantJoin(member);
                return true;
            }
        }
        return false;
    }

    /**
     * Closes the registration for this game.
     */
    public final void closeRegistration() {
        if (state == GameState.OPEN_REGISTRATION) {
            setState(GameState.CLOSED_REGISTRATION);
            sendMessage("The registrations for " + getGameName() + " have been **closed**!").submit();
        }
    }

    /**
     * Gets the channel id that this game uses to send messages.
     * @return the channel id that this game uses.
     */
    public final long getChannelId() {
        return channel.getIdLong();
    }

    /**
     * Gets the date & time when the game was created.
     * @return the date & time when the game was created.
     */
    public final Date getCreationDate() {
        return creation;
    }

    /**
     * Gets the date & time when the game ended.
     * @return the date & time when the game ended.
     */
    public final Date getEndDate() {
        return end;
    }

    /**
     * Gets the date & time when the game started.
     * @return the date & time when the game started.
     */
    public final Date getStartDate() {
        return start;
    }

    /**
     * Gets the ID of this game.
     * @return the id.
     */
    public final String getGameId() {
        return gameId;
    }

    /**
     * Gets the name of the game.
     * @return the game's name.
     */
    public final String getGameName() {
        if (getGameId() == null || getGameId().length() == 0)
            return type.name();
        return getGameId();
    }

    /**
     * Gets the state of the game.
     * @return the game's state.
     */
    public final GameState getGameState() {
        return state;
    }

    /**
     * Gets the type of this game.
     * @return the game's type.
     */
    public final GameType getGameType() {
        return type;
    }

    /**
     * Gets the guild id that this game is connected to.
     * @return the guild id.
     */
    public final long getGuildId() {
        return guild.getIdLong();
    }

    /**
     * Gets the ID of the user who owns/created the game.
     * @return the owner's id.
     */
    public final long getOwnerId() {
        return owner.getUser().getIdLong();
    }

    /**
     * Gets the number of people playing this game.
     * @return the number of players.
     */
    public abstract int getPlayerCount();

    /**
     * Checks if the specified user is the owner.
     * @param memberId the user's id.
     * @return true if that user is the owner, false otherwise.
     */
    public final boolean isOwner(long memberId) {
        return memberId == getOwnerId();
    }

    /**
     * Checks if a user is playing the game.
     * @param memberId the user's id
     * @return true if the user is a participant, false otherwise.
     */
    public abstract boolean isPlaying(long memberId);

    /**
     * Runs upon a new guild member joins the guild.
     * @param member the new member.
     */
    public abstract void onGuildMemberJoin(Member member);

    /**
     * Runs upon a guild member leaves the guild.
     * @param member the leaving member.
     */
    public abstract void onGuildMemberLeave(Member member);

    /**
     * Runs upon a message is received.
     * @param message the new message.
     */
    public abstract void onMessageReceived(Message message);

    /**
     * Executes when a member joins the game.
     * @param member the joining participant.
     */
    public abstract void onParticipantJoin(Member member);

    /**
     * Executes when a member leaves the game.
     * @param member the leaving participant.
     */
    public abstract void onParticipantLeave(Member member);

    /**
     * Executes when the game starts.
     */
    protected abstract void onStart();

    /**
     * Executes when the game stops regardless if it was forced.
     */
    protected abstract void onStop();

    /**
     * Executes when a new text channel is created.
     * @param channel the new channel.
     */
    public abstract void onTextChannelCreate(MessageChannel channel);

    /**
     * Executes when a text channel is deleted.
     * @param channel the deleted channel.
     */
    public abstract void onTextChannelDelete(MessageChannel channel);

    /**
     * Opens the registration for the game.
     */
    public final void openRegistration() {
        if (state == GameState.PREREGISTRATION) {
            setState(GameState.OPEN_REGISTRATION);
            sendMessage("The registrations are now **open**!").submit();
        } else if (state == GameState.CLOSED_REGISTRATION) {
            setState(GameState.OPEN_REGISTRATION);
            sendMessage("The registrations are once again **open**!").submit();
        }
    }

    /**
     * Kicks a participant from the game.
     * @param memberId the member's id to kick
     */
    public final void remove(long memberId) {
        if (isPlaying(memberId)) {
            Member participant = ServerManager.getInstance().get(getGuildId()).getMemberDetail(memberId).getMember();
            onParticipantLeave(participant);
        }
    }

    /**
     * Sends a message to the connected channel.
     * @param message the message to send.
     * @return the RestAction of type message.
     */
    public final RestAction<Message> sendMessage(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getGameName()).append("] ").append(message);
        return channel.sendMessage(sb.toString());
    }

    /**
     * Changes the owner of the game.
     * @param memberId the new owner's id.
     * @return true if the change was successful, false otherwise.
     */
    public final boolean setOwner(long memberId) {
        Member member = ServerManager.getInstance().get(getGuildId()).getMemberDetail(memberId).getMember();
        if (member != null) {
            owner = member;
            return true;
        }
        return false;
    }

    /**
     * Changes the state of the game.
     * @param state the new state of the game.
     */
    private void setState(GameState state) {
        this.state = state;
    }

    /**
     * Starts the game.
     */
    public final void start() {
        if (state == GameState.CLOSED_REGISTRATION || state == GameState.DONE) {
            sendMessage("The " + getGameType() + " game has **started**!").submit();
            setState(GameState.ONGOING);
            start = new Date();
            onStart();
        }
    }

    /**
     * Stops the game.
     */
    public final void stop() {
        if (state == GameState.ONGOING) {
            sendMessage("The " + getGameType() + " game has **stopped**!").submit();
            setState(GameState.DONE);
            end = new Date();
            onStop();
        }
    }
}
