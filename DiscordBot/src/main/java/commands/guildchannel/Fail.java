package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * This tests whether level requirements work.
 * Guild owners bypass all requirements.
 *
 * @author Tom
 */
public class Fail extends GuildChannelCommand {
    protected Fail() {
        super("fail", "", "Shows that level matters for everyone but the owner", GuildChannelCommandSet.GENERAL, (byte) 99);
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
        channel.sendMessage("Hi, " + member.getUser().getAsMention() + "!").submit();
        return CommandResult.SUCCESS;
    }
}
