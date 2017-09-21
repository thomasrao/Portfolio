package commands.guildchannel;

import commands.CommandResult;
import core.server.MemberManager;
import core.server.ServerManager;
import core.server.channel.ChannelInfo;
import core.server.guild.GuildInfo;
import games.GameState;
import games.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.text.SimpleDateFormat;

/**
 * This takes care of everything related
 *  to creating, deleting, stopping, starting
 *  a game in a channel/server.
 *
 * @author Tom
 */
public class Game extends GuildChannelCommand {
    private final static String LIST = "list";
    private final static String CREATE = "create";
    private final static String DELETE = "delete";
    private final static String REGISTRATION = "registration";
    private final static String JOIN = "join";
    private final static String LEAVE = "leave";
    private final static String ADD = "add";
    private final static String EXPEL = "expel";
    private final static String INFO = "info";
    private final static String START = "start";
    private final static String STOP = "stop";
    private final static String TRANSFER = "transfer";


    protected Game() {
        super("game", "<" + LIST + '/' + CREATE + '/' + /*DELETE + '/' +*/ REGISTRATION + '/' + JOIN + '/' + LEAVE + '/' + ADD + '/' + EXPEL + '/' + INFO + '/' + START + '/' + STOP + '/' + TRANSFER + "> [...]", "Manages games", GuildChannelCommandSet.GENERAL, (byte) 3);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        String[] parts = message.getRawContent().toLowerCase().split(" ");
        if (parts.length < 2)
            return CommandResult.SYNTAX;

        GuildInfo guildInfo = ServerManager.getInstance().get(guild.getIdLong());
        ChannelInfo channelInfo = guildInfo.getTextChannel(channel.getIdLong());
        games.Game game = channelInfo.getGameManager().getGame();
        String prefix = guildInfo.getPrefix();

        switch (parts[1]) {
            case LIST: { // Lists the games that are playable.
                StringBuilder sb = new StringBuilder();
                sb.append("**List of games**```");
                for (GameType g : GameType.values()) {
                    if (g.isVisible()) {
                        sb.append("\n  ").append(g.name()).append(": ").append(g.getDescription());
                        sb.append("\n  ").append("Maximum players: ").append(g.getMaximumPlayerCount());
                    }
                }
                sb.append("\n```");

                channel.sendMessage(sb.toString()).submit();
            }
            break;
            case CREATE: { // Creates a game in the current channel.
                if (parts.length < 3) {
                    channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + CREATE + " <game_name>").submit();
                    return CommandResult.FAILURE;
                }

                GameType type;
                try {
                    type = GameType.valueOf(parts[2].toUpperCase());
                    if (type == GameType.UNKNOWN)
                        throw new IllegalArgumentException("type");
                } catch (IllegalArgumentException iae) {
                    channel.sendMessage("There is no such game called " + parts[2] + ".").submit();
                    return CommandResult.SUCCESS;
                }

                // TODO: allow game ids/names
                if (game == null || game.getGameState() == GameState.DONE) {
                    channelInfo.getGameManager().create("", type, member.getUser().getIdLong());
                    channel.sendMessage("A game of " + type.name() + " has been created! It exists within this channel only.").submit();
                } else
                    channel.sendMessage("There is already another game running: " + game.getGameType().name() + ".").submit();
            }
            break;
            case DELETE: { // Deletes the game from the current channel.
                if (game == null) {
                    channel.sendMessage("There is no game to delete in this channel, " + member.getEffectiveName()).submit();
                    return CommandResult.SUCCESS;
                }

                MemberManager gameOwner = guildInfo.getMemberDetail(game.getOwnerId());
                MemberManager me = guildInfo.getMemberDetail(member.getUser().getIdLong());

                if (member.isOwner() || game.getGameState() == GameState.DONE || me.getId() == game.getOwnerId() || me.getCommandLevel() >= gameOwner.getCommandLevel() && game.getGameState() == GameState.PREREGISTRATION) {
                    channelInfo.getGameManager().delete();
                    channel.sendMessage(gameOwner.getUsername() + "'s game has been deleted by " + me.getUsername() + ".").submit();
                } else
                    game.sendMessage(me.getUsername() + ", you lacked the permission to delete the game.").submit();
            }
            break;
            case REGISTRATION: { // Opens or closes registration
                if (parts.length < 3 || !parts[2].equals("open") && !parts[2].equals("close")) {
                    channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + REGISTRATION + " <open/close>").submit();
                    return CommandResult.FAILURE;
                } else if (game == null) {
                    channel.sendMessage("There is no game session in this channel, " + member.getEffectiveName() + '.').submit();
                    return CommandResult.SUCCESS;
                }

                if (game == null) {
                    channel.sendMessage("This game does not require registration.").submit();
                } else if (game.getGameState() == GameState.PREREGISTRATION || game.getGameState() == GameState.CLOSED_REGISTRATION) {
                    if (parts[2].equals("open")) {
                        game.openRegistration();
                    } else
                        game.sendMessage("The game has already opened its registration!").submit();
                } else if (game.getGameState() == GameState.OPEN_REGISTRATION) {
                    if (parts[2].equals("close")) {
                        game.closeRegistration();
                    } else
                        game.sendMessage("The game has already closed its registration!").submit();
                } else
                    game.sendMessage("The game has already begun or has ended, so no registrations can be done, " + member.getEffectiveName() + '.').submit();
            }
            break;
            case JOIN: { // Joins the game in the current channel.
                if (game == null) {
                    channel.sendMessage("There is no game running in this channel, " + member.getEffectiveName() + ".").submit();
                } else if (game.getGameState() == GameState.OPEN_REGISTRATION) {
                    if (game.add(member.getUser().getIdLong()))
                        game.sendMessage("You successfully joined the game, " + member.getEffectiveName() + "!").submit();
                    else if (game.isPlaying(member.getUser().getIdLong()))
                        game.sendMessage("You are already a participant of the game, " + member.getEffectiveName() + '.').submit();
                    else
                        game.sendMessage("The game is full, " + member.getEffectiveName() + "!").submit();
                } else if (game.getGameState() == GameState.PREREGISTRATION)
                    game.sendMessage("The registrations have not yet opened, " + member.getEffectiveName() + '.').submit();
                else if (game.getGameState() == GameState.CLOSED_REGISTRATION)
                    game.sendMessage("The registrations have been closed, " + member.getEffectiveName() + '.').submit();
                else if (game.getGameState() == GameState.ONGOING)
                    game.sendMessage("The game is already ongoing, " + member.getEffectiveName() + '.').submit();
                else
                    game.sendMessage("The game is over, " + member.getEffectiveName() + '.').submit();
            }
            break;
            case LEAVE: { // Leaves the game in the current channel.
                if (game == null) {
                    channel.sendMessage("There is no game running, " + member.getEffectiveName() + '.').submit();
                } else if (!game.isPlaying(member.getUser().getIdLong())) {
                    game.sendMessage("You are not part of the game, " + member.getEffectiveName() + '.').submit();
                } else {
                    game.remove(member.getUser().getIdLong());
                    game.sendMessage("You have left the game, " + member.getEffectiveName() + '.').submit();
                }
            }
            break;
            case ADD: {
                if (parts.length < 3) {
                    channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + ADD + " <user_mention>").submit();
                    return CommandResult.FAILURE;
                } else if (game == null) {
                    channel.sendMessage("There is no game running, " + member.getEffectiveName() + '.').submit();
                } else if (!member.isOwner() && !game.isOwner(member.getUser().getIdLong())) {
                    game.sendMessage("You are not the owner of the game, " + member.getEffectiveName() + ".").submit();
                } else {
                    // Fetching the user id from the user mention.
                    long memberId;
                    try {
                        memberId = Long.parseLong(parts[2].substring(3, parts[1].length() - 1));
                    } catch (NumberFormatException nfe) {
                        channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + TRANSFER + " <user_mention>").submit();
                        return CommandResult.FAILURE;
                    }

                    // Checking if the member exists within the current guild.
                    MemberManager participant = ServerManager.getInstance().get(guild.getIdLong()).getMemberDetail(memberId);
                    if (participant == null) {
                        channel.sendMessage("The member must exists within this guild.").submit();
                        return CommandResult.FAILURE;
                    }

                    if (game.isPlaying(participant.getId())) {
                        game.sendMessage(participant.getUsername() + " is already playing, " + member.getEffectiveName() + '.');
                    } else {
                        game.add(participant.getId());
                        if (game.isPlaying(participant.getId()))
                            game.sendMessage(participant.getUsername() + " has joined the game! Added by " + member.getEffectiveName() + '.').submit();
                        else
                            game.sendMessage("The game cannot add another player, whether be the limit has been reached or some other reason.").submit();
                    }
                }
            }
            break;
            case EXPEL: {
                if (parts.length < 3) {
                    channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + EXPEL + " <user_mention>").submit();
                    return CommandResult.FAILURE;
                } else if (game == null) {
                    channel.sendMessage("There is no game running, " + member.getEffectiveName() + '.').submit();
                } else if (!member.isOwner() && !game.isOwner(member.getUser().getIdLong())) {
                    game.sendMessage("You are not the owner of the game, " + member.getEffectiveName() + ".").submit();
                } else {
                    // Fetching the user id from the user mention.
                    long memberId;
                    try {
                        memberId = Long.parseLong(parts[2].substring(3, parts[1].length() - 1));
                    } catch (NumberFormatException nfe) {
                        channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + TRANSFER + " <user_mention>").submit();
                        return CommandResult.FAILURE;
                    }

                    // Checking if the member exists within the current guild.
                    MemberManager participant = ServerManager.getInstance().get(guild.getIdLong()).getMemberDetail(memberId);
                    if (participant == null) {
                        channel.sendMessage("The member must exists within this guild.").submit();
                        return CommandResult.FAILURE;
                    }

                    if (game.isPlaying(memberId)) {
                        game.add(memberId);
                        game.sendMessage(participant.getUsername() + " has been expelled from the game! Removed by " + member.getEffectiveName() + '.').submit();
                    } else {
                        game.sendMessage(participant.getUsername() + " is not playing, " + member.getEffectiveName() + '.');
                    }
                }
            }
            break;
            case INFO: { // Displays information about the current game.
                StringBuilder sb = new StringBuilder();
                sb.append("**Game Information**\n");

                if (game == null) {
                    sb.append("```\nNo game available in this channel.\n```");
                } else {
                    MemberManager gameOwner = guildInfo.getMemberDetail(game.getOwnerId());

                    sb.append("```\n");
                    sb.append("Game: ").append(game.getGameType().name()).append('\n');
                    sb.append("State: ").append(game.getGameState().name()).append('\n');

                    if (gameOwner != null)
                        sb.append("Owner: ").append(gameOwner.getUsername()).append('\n');

                    sb.append("Player count: ").append(game.getPlayerCount()).append('\n');
                    sb.append("Creation date & time: ").append(SimpleDateFormat.getInstance().format(game.getCreationDate())).append('\n');
                    if (game.getGameState() == GameState.ONGOING || game.getGameState() == GameState.DONE) {
                        sb.append("Start date & time: ").append(SimpleDateFormat.getInstance().format(game.getStartDate())).append('\n');
                        if (game.getGameState() == GameState.DONE)
                            sb.append("End date & time: ").append(SimpleDateFormat.getInstance().format(game.getEndDate())).append('\n');
                    }
                    sb.append("\n```");
                }
                channel.sendMessage(sb.toString()).submit();
            }
            break;
            case START: { // Starts the game
                if (game == null) {
                    channel.sendMessage("There is no game running in this channel, " + member.getEffectiveName() + ".").submit();
                } else if (!member.isOwner() && !game.isOwner(member.getUser().getIdLong())) {
                    game.sendMessage("You are not the owner of the game, " + member.getEffectiveName() + ".").submit();
                } else if (game.getGameState() == GameState.CLOSED_REGISTRATION || game.getGameState() == GameState.DONE) {
                    game.start();
                }
            }
            break;
            case STOP: { // Stops the game
                if (game == null) {
                    channel.sendMessage("There is no game running in this channel, " + member.getEffectiveName() + ".").submit();
                } else if (!member.isOwner() && !game.isOwner(member.getUser().getIdLong())) {
                    game.sendMessage("You are not the owner of the game, " + member.getEffectiveName() + ".").submit();
                } else {
                    game.stop();
                }
            }
            break;
            case TRANSFER: { // Transfers ownership
                if (parts.length < 3) {
                    channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + TRANSFER + " <user_mention>").submit();
                    return CommandResult.FAILURE;
                } else if (game == null)
                    channel.sendMessage("There is no game running in this channel, " + member.getEffectiveName() + ".").submit();
                else if (!game.isOwner(member.getUser().getIdLong())) {
                    game.sendMessage("You are not the owner of the game, " + member.getEffectiveName() + ".").submit();
                } else {
                    // Fetching the user id from the user mention.
                    long memberId;
                    try {
                        memberId = Long.parseLong(parts[2].substring(3, parts[1].length() - 1));
                    } catch (NumberFormatException nfe) {
                        channel.sendMessage("Invalid syntax for `" + getName() + "` command.\nCommand usage: " + prefix + getName() + ' ' + TRANSFER + " <user_mention>").submit();
                        return CommandResult.FAILURE;
                    }

                    // Checking if the member exists within the current guild.
                    MemberManager participant = ServerManager.getInstance().get(guild.getIdLong()).getMemberDetail(memberId);
                    if (participant == null) {
                        channel.sendMessage("The member must exists within this guild.").submit();
                        return CommandResult.FAILURE;
                    }

                    if (memberId != member.getUser().getIdLong()) {
                        game.setOwner(memberId);
                        String name = message.getMentionedUsers().get(0).getName();
                        game.sendMessage("The ownership of this game has successfully been moved to " + name + '.').submit();
                    } else
                        game.sendMessage("You are already the owner, " + member.getEffectiveName() + '.').submit();
                }
            }
            break;
            default:
                return CommandResult.SYNTAX;
        }
        return CommandResult.SUCCESS;
    }
}
