package commands.guildchannel;

import commands.CommandResult;
import core.server.MemberManager;
import core.server.ServerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Changes the level of a user of the guild.
 *
 * @author Tom
 */
public class SetLevel extends GuildChannelCommand {
    protected SetLevel() {
        super("setlevel", "<user_mention> <level>", "Changes the level of a user", GuildChannelCommandSet.GENERAL, (byte) 5);
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
        if (parts.length < 3)
            return CommandResult.SYNTAX;

        if (parts[1].matches("^<@![\\d]+>$")) {
            long memberId, guildId = guild.getIdLong();
            byte level;

            // Get the member ID specified, first argument.
            // Get the level from the second argument.
            try {
                memberId = Long.parseLong(parts[1].substring(3, parts[1].length() - 1));
                level = Byte.parseByte(parts[2]);
            } catch (NumberFormatException nfe) {
                return CommandResult.SYNTAX;
            }

            MemberManager me = ServerManager.getInstance().get(guildId).getMemberDetail(member.getUser().getIdLong());
            MemberManager victim = ServerManager.getInstance().get(guildId).getMemberDetail(memberId);

            // Check if the member has permission to change level
            // of the targeted user.
            if (member.isOwner()) {
                victim.setCommandLevel(level);
                channel.sendMessage(victim.getUsername() + "'s level has been set to " + level + " by " + me.getUsername() + ".").submit();
            } else if (me.getCommandLevel() > victim.getCommandLevel()) {
                if (me.getCommandLevel() > level) {
                    victim.setCommandLevel(level);
                    channel.sendMessage(victim.getUsername() + "'s level has been set to " + level + " by " + me.getUsername() + ".").submit();
                } else {
                    channel.sendMessage(me.getUsername() + ", you cannot change someone's level to a level that is higher or equal to yours.").submit();
                }
            } else {
                channel.sendMessage(me.getUsername() + ", you cannot change someone's level who is equal or above yours.").submit();
            }
            return CommandResult.SUCCESS;
        }

        return CommandResult.SYNTAX;
    }
}
