package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * This sends the link for the bot's invite link
 *  to the channel the message was received in.
 *
 * @author Jay
 */
public class Invite extends GuildChannelCommand {
    protected Invite() {
        super("Invite", "", "Invite me to another server", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        channel.sendMessage("https://discordapp.com/oauth2/authorize?&client_id=318871492953636864&scope=bot&permissions=0").submit();
        return CommandResult.SUCCESS;
    }
}
