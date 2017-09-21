package commands.privatechannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

/**
 * This sends the list of commands available
 * or sends information regarding a specific command
 * to the channel.
 *
 * @author Tom
 */
public class Help extends PrivateChannelCommand {
    public Help() {
        super("help", "[command_name]", "Gives a list of command or some information about a specific command.");
    }

    /**
     * Executes upon the request of this command through private messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(PrivateChannel channel, Member member, Message message) {
        // Display the help required.
        String[] parts = message.getContent().split(" ");
        if (parts.length < 2 || !PrivateChannelCommandManager.getInstance().has(parts[1]))
            channel.sendMessage(PrivateChannelCommandManager.getInstance().toString()).submit();
        else
            channel.sendMessage(PrivateChannelCommandManager.getInstance().get(parts[1]).toString()).submit();
        return CommandResult.SUCCESS;
    }
}
