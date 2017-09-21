package commands.guildchannel;

import commands.CommandResult;
import core.server.ServerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Changes the command prefix which the guild will use.
 *
 * @author Tom
 */
public class SetPrefix extends GuildChannelCommand {
    protected SetPrefix() {
        super("setprefix", "<new_prefix>", "Changes the command prefix", GuildChannelCommandSet.GENERAL, (byte) 10);
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
        int spaceIndex = message.getRawContent().indexOf(' ');
        if (spaceIndex == -1)
            return CommandResult.SYNTAX;

        // Changes the prefix required to execute commands.
        String prefix = message.getContent().split(" ")[1]; //message.getContent().substring(spaceIndex + 1);
        ServerManager.getInstance().get(guild.getIdLong()).setPrefix(prefix);
        channel.sendMessage("**Command prefix has been changed to `" + prefix + "`.**").submit();
        return CommandResult.SUCCESS;
    }
}
