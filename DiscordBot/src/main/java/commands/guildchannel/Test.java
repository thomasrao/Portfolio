package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * A simple command for testing purposes:
 *  a reply is sent back to test if the bot's
 *  command system is working.
 *
 * @author Tom
 */
public class Test extends GuildChannelCommand {
    public Test() {
        super("test", "", "Testing", GuildChannelCommandSet.GENERAL, (byte) 0);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        if (message.getContent().contains(" ")) {
            return CommandResult.SYNTAX;
        }

        channel.sendMessage("Success!").submit();
        return CommandResult.SUCCESS;
    }
}
