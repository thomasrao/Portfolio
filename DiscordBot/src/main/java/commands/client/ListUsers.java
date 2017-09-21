package commands.client;

import commands.CommandResult;
import core.server.MemberManager;
import core.server.ServerManager;

import java.util.Collection;

/**
 * Lists the users in a specific guild that this bot is connected to.
 *
 * @author Tom
 */
public class ListUsers extends ClientCommand {
    protected ListUsers() {
        super("listusers", "<guild_id>", "List the users with the given guild ID if available.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 2)
            return CommandResult.SYNTAX;

        // Get the guild ID from the first argument.
        long guildId;
        try {
            guildId = Long.parseLong(parts[1]);
        } catch (NumberFormatException nfe) {
            return CommandResult.SYNTAX;
        }

        // Output list of users found.
        Collection<MemberManager> members = ServerManager.getInstance().get(guildId).getMemberDetails().values();

        StringBuilder sb = new StringBuilder();
        sb.append("List of users in guild #").append(guildId).append(":\n");
        for (MemberManager member : members) {
            sb.append(member.getUsername()).append(" (").append(member.getNickname()).append("; ").append(member.getId());
            sb.append(", level: ").append(member.getCommandLevel()).append(")\n");
        }

        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
        return CommandResult.SUCCESS;
    }
}
