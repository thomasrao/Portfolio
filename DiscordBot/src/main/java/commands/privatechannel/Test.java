package commands.privatechannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

/**
 * A simple command for testing purposes:
 *  a reply is sent back to test if the bot's
 *  command system is working.
 *
 * @author Tom
 */
public class Test extends PrivateChannelCommand {

    public Test() {
        super("test", "", "Testing commands!");
    }

    /**
     * Executes upon the request of this command through private messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(PrivateChannel channel, Member member, Message message) {
        if (message.getContent().contains(" ")) {
            return CommandResult.SYNTAX;
        }

        channel.sendMessage("Success!").submit();
        return CommandResult.SUCCESS;
    }
}
