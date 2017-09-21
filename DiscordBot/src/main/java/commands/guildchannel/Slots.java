package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Allows a user to play a game of slots.
 * Under construction.
 *
 * @author Tom
 */
public class Slots extends GuildChannelCommand {
    public Slots() {
        super("slots", "(bet_amount)", "Testing", GuildChannelCommandSet.GENERAL, (byte) 0);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        // TODO: implement the command to play slots.
        return CommandResult.SUCCESS;
    }
}
