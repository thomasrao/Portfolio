package commands.client;

import commands.CommandResult;
import core.server.MemberManager;
import core.server.ServerManager;
import core.server.guild.GuildInfo;

/**
 * Changes the level of a user of a guild connected to.
 *
 * @author Tom
 */
public class SetLevel extends ClientCommand {
    protected SetLevel() {
        super("setlevel", "<guild_id> <user_id> <new_level>", "Changes the level of a user in a specific guild.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 4)
            return CommandResult.SYNTAX;

        try {
            // Get the guild, member and level from
            // the first, second, third argument, respectively.
            long guildId = Long.parseLong(parts[1]);
            long memberId = Long.parseLong(parts[2]);
            byte level = Byte.parseByte(parts[3]);

            GuildInfo guild = ServerManager.getInstance().get(guildId);

            // Check if we're connected to that guild.
            if (guild != null) {
                MemberManager member = guild.getMemberDetail(memberId);

                // Check if the member exists.
                if (member != null) {
                    // Change his level.
                    member.setCommandLevel(level);
                } else if (guild.getMemberDetail(memberId) != null) {
                    guild.addMember(guild.getMemberDetail(memberId).getMember(), level);
                } else {
                    System.out.println("Unable to find member #" + memberId + " in guild #" + guildId);
                    return CommandResult.SUCCESS;
                }
                System.out.println("Changed the level of user #" + memberId + " from guild #" + guildId + " to " + level);
            } else {
                System.out.println("Unable to find guild #" + guildId);
                return CommandResult.FAILURE;
            }
        } catch (NumberFormatException nfe) {
            return CommandResult.SYNTAX;
        }
        return CommandResult.SUCCESS;
    }
}
