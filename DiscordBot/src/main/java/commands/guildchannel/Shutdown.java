package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Shuts down the bot.
 *
 * @author Tom
 */
public class Shutdown extends GuildChannelCommand {
    public Shutdown() {
        super("shutdown", "", "Testing", GuildChannelCommandSet.GENERAL, (byte) 127);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        if (message.getContent().contains("now")) {
            return CommandResult.SYNTAX;
        }

        // Shut down the bot.
        channel.sendMessage("Bot is committing suicide...").submit();
        guild.getJDA().shutdownNow();
        return CommandResult.SUCCESS;
    }
}
