package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.List;

/**
 * This sends the list of commands available
 * or sends information regarding a specific command
 * to the channel.
 *
 * @author Tom
 */
public class Help extends GuildChannelCommand {
    public Help() {
        super("help", "[command_name]", "Gives a list of command or some information about a specific command", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        // Check if the help is not for a specific command.
        String[] parts = message.getContent().split(" ");
        if (parts.length < 2 || !GuildChannelCommandManager.getInstance().has(parts[1])) {
            // Output list of commands
            // Message may exceed the limit, so the list
            // may be separated into several messages.
            List<String> helpPages = GuildChannelCommandManager.getInstance().getHelpPages(false);
            for (String help : helpPages)
                channel.sendMessage(help).submit();
        } else
            // Display information regarding the command specified.
            channel.sendMessage(GuildChannelCommandManager.getInstance().get(parts[1]).toString()).submit();
        return CommandResult.SUCCESS;
    }
}
