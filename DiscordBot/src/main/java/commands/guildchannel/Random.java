package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Generates a random number between 1 and a number,
 *  which is either specified by the user or it is
 *  long's maximum value.
 *
 * @author Tom
 */
public class Random extends GuildChannelCommand {
    private final static java.util.Random random;


    static {
        random = new java.util.Random();
    }

    protected Random() {
        super("random", "[maximum]", "Provides a random number where the maximum is excluded", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        long maximum = Long.MAX_VALUE; // Default maximum value

        // Get the maximum value from the first argument if available.
        String[] parts = message.getContent().split(" ");
        if (parts.length > 1) {
            try {
                maximum = Long.parseLong(parts[1]);
            } catch (NumberFormatException nfe) {
                // Ignore error.
            }
        }

        // Pick a random number.
        long random = (((long) this.random.nextInt() << 32) | (long) this.random.nextInt()) % maximum;
        System.out.println(random);
        channel.sendMessage(String.valueOf(random)).submit();
        return CommandResult.SUCCESS;
    }
}
