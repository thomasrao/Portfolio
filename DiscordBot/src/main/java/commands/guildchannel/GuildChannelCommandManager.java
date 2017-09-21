package commands.guildchannel;

import commands.CommandResult;
import commands.CommandSetManager;
import core.server.ServerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.logging.Log;

/**
 * The command manager for guild channels decides
 *  how every command is stored.
 * It also decides how the help information
 *  is displayed to the chat.
 *
 * @author Tom
 */
public final class GuildChannelCommandManager extends CommandSetManager<GuildChannelCommand, GuildChannelCommandSet> {
    private final static GuildChannelCommandManager instance;


    static {
        instance = new GuildChannelCommandManager();
    }

    public GuildChannelCommandManager() {
        super();

        // General Commands
        add(new AddShitPost());
        add(new EmojiLetters());
        //add(new Fail());
        add(new Game());
        add(new Help());
        add(new Info());
        add(new Invite());
        add(new Random());
        add(new Reminder());
        add(new SetLevel());
        add(new SetPrefix());
        add(new ShitPost());
        add(new Shutdown());
        add(new Test());

        // Search Commands:
        add(new GoogleImageSearch());
        add(new GoogleSearch());

        // Audio Commands:
        add(new AudioClear());
        add(new AudioInQueued());
        add(new AudioJoin());
        add(new AudioLeave());
        add(new AudioPause());
        add(new AudioRepeat());
        add(new AudioRepeatOne());
        add(new AudioRequest());
        add(new AudioResume());
        add(new AudioShuffle());
        add(new AudioSkip());
        add(new AudioSong());
    }


    /**
     * Gets the instance of this class.
     * @return the instance of this class.
     */
    public static GuildChannelCommandManager getInstance() {
        return instance;
    }

    /**
     * This executes a command & decides what should happen
     *  before & after a command is executed.
     * @param guild the guild in which the command will be executed in.
     * @param channel the channel of the guild.
     * @param member the member who wants to execute this command.
     * @param message the message which requested this command, which includes the command's arguments.
     * @param prefix the prefix used.
     * @return true if the command executed regardless of its result, false otherwise.
     */
    public final boolean run(Guild guild, MessageChannel channel, Member member, Message message, String prefix) {
        // Ignore messages from bots and messages that do not start with the prefix.
        if (member.getUser().isBot() || !message.getContent().startsWith(prefix))
            return false;

        // Get the command used
        String m = message.getContent().substring(prefix.length());
        int spaceIndex = m.indexOf(' ', prefix.length());
        int endIndex = spaceIndex == -1 ? m.length() : spaceIndex;
        String commandName = m.substring(0, endIndex);

        // Check if the command exists.
        if (has(commandName)) {
            long guildId = guild.getIdLong();
            long memberId = member.getUser().getIdLong();

            // Check whether this member meets the level requirements.
            if (!member.isOwner() && ServerManager.getInstance().get(guildId).getMemberDetail(memberId).getCommandLevel() < get(commandName).getLevel()) {
                channel.sendMessage("This command is reserved for level " + get(commandName).getLevel() + " and higher users.").submit();
                return false;
            }

            // Execute the command and get the result.
            GuildChannelCommand command = get(commandName);
            CommandResult result = command.handle(guild, channel, member, message);

            // Output or log if required.
            switch (result) {
                case SUCCESS:
                    break;
                case SYNTAX:
                    channel.sendMessage("Invalid syntax for `" + commandName + "` command.\nCommand usage: " + prefix + commandName + ' ' + command.getSyntax()).submit();
                    break;
                case FAILURE:
                    break;
                default:
                    Log.getInstance().warn("New command result found: " + (result == null ? "null" : result.name()) + ".", "GuildCommandManager");
                    break;
            }

            return true;
        }
        return false;
    }
}
