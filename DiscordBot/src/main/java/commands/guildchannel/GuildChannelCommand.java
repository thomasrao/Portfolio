package commands.guildchannel;

import commands.CommandResult;
import commands.CommandSet;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * This is a command, a way to invoke a set of methods.
 * It describes its name, which will be how it will get
 *  invoked, its description, its syntax and whether
 *  it should be hidden from the help page.
 * This command is designed for guilds.
 *
 * @author Tom
 */
public abstract class GuildChannelCommand extends CommandSet<GuildChannelCommandSet> {
    private final byte level; // The minimum user level required to run this command.


    protected GuildChannelCommand(String command, String syntax, String description, GuildChannelCommandSet set, byte level) {
        super(command, syntax, description, set);

        this.level = level;
    }


    /**
     * Gets the minimum user level required to execute this command.
     * @return
     */
    public final byte getLevel() {
        return level;
    }

    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public abstract CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message);


    /**
     * Gets information about the command, including its syntax.
     * @return information about the command.
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(getName()).append("**: ").append(getDescription()).append(" (level ").append(level).append(").");

        if (getSyntax().length() > 0)
            sb.append("\n  Parameters: ").append(getSyntax());
        return sb.toString();
    }
}
