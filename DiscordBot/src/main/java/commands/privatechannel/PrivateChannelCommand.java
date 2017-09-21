package commands.privatechannel;

import commands.Command;
import commands.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;

/**
 * This is a command, a way to invoke a set of methods.
 * It describes its name, which will be how it will get
 *  invoked, its description, its syntax and whether
 *  it should be hidden from the help page.
 * This command is designed for private channels.
 *
 * @author Tom
 */
public abstract class PrivateChannelCommand extends Command {
    protected PrivateChannelCommand(String command, String syntax, String description) {
        super(command, syntax, description);
    }

    /**
     * Executes upon the request of this command through private messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public abstract CommandResult handle(PrivateChannel channel, Member member, Message message);

    /**
     * Gets information about this command, such as its syntax.
     * @return information regarding this command.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(getName()).append("**: ").append(getDescription());

        if (getSyntax().length() > 0)
            sb.append("\n    Command parameters: ").append(getSyntax());
        return sb.toString();
    }
}
