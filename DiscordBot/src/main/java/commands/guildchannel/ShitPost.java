package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.factories.ShitpostFactory;

/**
 * Sends a random message out of a list of messages registered.
 *
 * @author Jay
 */
public class ShitPost extends GuildChannelCommand {
    public ShitPost(){
        super("ShitPost", "", "Shit posts funny things ***[Trigger Warning]***", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        // Send a random shitpost message from the list of available messages.
        String shitpost = ShitpostFactory.getInstance().getRandom();
        if (shitpost != null)
            channel.sendMessage(shitpost).submit();
        //message.addReaction(guild.getEmotes().get(1));
        return CommandResult.SUCCESS;
    }

}
