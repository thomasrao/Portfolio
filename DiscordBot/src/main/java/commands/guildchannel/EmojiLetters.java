package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Sends a message, where each letter and some punctuation
 *  as an emoji using Discord's default set of emojis.
 *
 * @author Tom
 */
public class EmojiLetters extends GuildChannelCommand {
    private final static int MAXIMUM_MESSAGE_LENGTH = 2000;  // Maximum message length
    private final static String[] NUMBERS = new String[] {":zero:", ":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:"};
    private final static String QUESTION_MARK = ":question:";        // Emoji for question mark
    private final static String EXCLAMATION_MARK = ":exclamation:";  // Emoji for exclamation mark.


    protected EmojiLetters() {
        super("emojiletters", "<message>", "Turns every letter into a corresponding emoji", GuildChannelCommandSet.GENERAL, (byte) 4);
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
        // Get the text after the first space.
        int spaceIndex = message.getRawContent().indexOf(' ');
        int endIndex = spaceIndex == -1 ? message.getRawContent().length() : spaceIndex;
        String m = message.getRawContent().substring(endIndex + 1);

        // Replace every alphanumerical value by its corresponding
        // emoji equivalent.
        StringBuilder sb = new StringBuilder();
        for (char c : m.toLowerCase().toCharArray()) {
            if (Character.isAlphabetic(c))
                sb.append(":regional_indicator_").append(c).append(':');
            else if (Character.isDigit(c))
                sb.append(NUMBERS[c - 0x30]);
            else if (c == '?')
                sb.append(QUESTION_MARK);
            else if (c == '!')
                sb.append(EXCLAMATION_MARK);
            else
                sb.append(c);
        }

        // Check if the message is short enough.
        if (sb.length() > MAXIMUM_MESSAGE_LENGTH)
            channel.sendMessage(sb.toString()).submit();
        else
            channel.sendMessage("The message is too long.").submit();
        return CommandResult.SUCCESS;
    }
}
