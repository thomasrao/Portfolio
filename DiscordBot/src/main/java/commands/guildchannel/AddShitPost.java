package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.factories.ShitpostFactory;

/**
 * Adds a message to the list of already defined messages.
 * These messages can be seen by running the shitpost command.
 *
 * @author Jay
 */
public class AddShitPost extends GuildChannelCommand {
    protected AddShitPost(){
        super("addshitpost","<shitpost_message>","Adds a shit post to the list", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        // Get the input after the first space as a new shitpost message.
        int spaceIndex = message.getContent().indexOf(" ");
        if (spaceIndex >= 0) {
            String shitpost = message.getContent().substring(spaceIndex + 1).trim();

            // Make sure the string isn't empty.
            if (shitpost.length() > 0) {
                ShitpostFactory.getInstance().add(shitpost);
                return CommandResult.SUCCESS;
            }
        }
        return CommandResult.SYNTAX;
    }
}
