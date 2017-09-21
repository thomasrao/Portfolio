package commands.privatechannel;

import commands.CommandManager;
import commands.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import tools.logging.Log;

/**
 * The command manager for private channels
 *  decides how every command is stored.
 * It also decides how the help information
 *  is displayed to the chat.
 *
 * @author Tom
 */
public final class PrivateChannelCommandManager extends CommandManager<PrivateChannelCommand> {
    private final static PrivateChannelCommandManager _instance;


    static {
        _instance = new PrivateChannelCommandManager();
    }

    public PrivateChannelCommandManager() {
        super();

        add(new Help());
        add(new Test());
    }


    /**
     * Gets the instance of this class.
     * @return the instance of this class.
     */
    public static PrivateChannelCommandManager getInstance() {
        return _instance;
    }

    /**
     * This executes a command & decides what should happen
     *  before & after a command is executed.
     * @param channel the private channel in which the command will be executed in.
     * @param member the member who wants to execute this command.
     * @param message the message which requested this command, which includes the command's arguments.
     * @param prefix the prefix used.
     * @return true if the command executed regardless of its result, false otherwise.
     */
    public final boolean run(PrivateChannel channel, Member member, Message message, String prefix) {
        if (!message.getContent().startsWith(prefix))
            return false;

        // Get the command name
        String m = message.getContent().substring(prefix.length());
        int spaceIndex = m.indexOf(' ', prefix.length());
        int endIndex = spaceIndex == -1 ? m.length() : spaceIndex;
        String commandName = m.substring(0, endIndex);

        // Check if that command exists.
        if (has(commandName)) {
            // Get & execute the command.
            PrivateChannelCommand command = get(commandName);
            CommandResult result = command.handle(channel, member, message);

            // Output & log if required.
            switch (result) {
                case SUCCESS:
                    break;
                case SYNTAX:
                    channel.sendMessage("Invalid syntax for `" + commandName + "` command.\nCommand usage: " + prefix + commandName + ' ' + command.getSyntax()).submit();
                    break;
                case FAILURE:
                    break;
                default:
                    Log.getInstance().warn("New command result found: " + (result == null ? "null" : result.name()) + ".", "PrivateCommandManager");
                    break;
            }

            return true;
        }
        return false;
    }
}
