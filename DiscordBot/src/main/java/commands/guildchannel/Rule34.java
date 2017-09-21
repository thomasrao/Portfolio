package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.rule34.Rule34Database;

/**
 * A terrible command.
 *
 * @author Tom
 */
public class Rule34 extends GuildChannelCommand {
    protected Rule34() {
        super("r34", "<search_term>", "Gets a random image from rule34.xxx - must be nsfw channel", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        if (channel.getName().toLowerCase().contains("nsfw")) {
            int spaceIndex = message.getContent().indexOf(' ');
            if (spaceIndex >= 0) {
                String search = message.getContent().substring(spaceIndex + 1);
                String result = Rule34Database.getInstance().fetch(search);
                if (result == null)
                    channel.sendMessage("No results found for " + search + ".").submit();
                else
                    channel.sendMessage(result).submit();
            } else
                return CommandResult.SYNTAX;
        } else
            channel.sendMessage("You may only use this in a channel that contains `nsfw` in its name.").submit();
        return CommandResult.SUCCESS;
    }
}
