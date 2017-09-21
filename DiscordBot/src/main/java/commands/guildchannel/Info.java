package commands.guildchannel;

import commands.CommandResult;
import core.server.MemberManager;
import core.server.ServerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Displays what level the sender is.
 *
 * @author Tom
 */
public class Info extends GuildChannelCommand {
    protected Info() {
        super("info", "[user_mention]", "Tells you the level of yourself or of the specified user", GuildChannelCommandSet.GENERAL, (byte) 0);
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
        String[] parts = message.getRawContent().split(" ");
        long memberId = member.getUser().getIdLong(), guildId = guild.getIdLong();

        // Check if the user has inputted a user mention
        // as first argument.
        if (parts.length >= 2) {
            if (parts[1].matches("^<@![\\d]+>$")) {
                // Get the member ID.
                try {
                    memberId = Long.parseLong(parts[1].substring(3, parts[1].length() - 1));

                    MemberManager victim = ServerManager.getInstance().get(guildId).getMemberDetail(memberId);

                    // Display the specified member's level
                    if (victim != null) {
                        channel.sendMessage(victim.getUsername() + " is level " + victim.getCommandLevel() + ".").submit();
                        return CommandResult.SUCCESS;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    // Ignoring errors.
                }
            }
        }

        // Otherwise, display the sender's level.
        channel.sendMessage("Your level is " + ServerManager.getInstance().get(guildId).getMemberDetail(memberId).getCommandLevel() + ".").submit();
        return CommandResult.SUCCESS;
    }
}
