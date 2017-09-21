package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Gets the URL for searching on a certain website.
 *
 * @author Tom
 */
public abstract class Search extends GuildChannelCommand {
    private String website;
    private String suffix;


    protected Search(String command, String websiteName, String website, String suffix) {
        super(command, "<search_term>", "Searches on " + websiteName, GuildChannelCommandSet.SEARCH, (byte) 0);

        this.website = website;
        this.suffix = suffix;
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
        channel.sendMessage(website + suffix + message.getContent().substring(message.getContent().indexOf(' ') + 1).replaceAll(" ", "%20")).submit();
        return CommandResult.SUCCESS;
    }
}