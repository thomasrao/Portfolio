package commands.client;

import commands.CommandResult;
import core.server.ServerManager;
import core.server.guild.GuildInfo;

import java.util.Collection;

/**
 * Lists the guilds that this bot is connected to.
 *
 * @author Tom
 */
public class ListGuilds extends ClientCommand {
    protected ListGuilds() {
        super("listguilds", "", "List the guilds that the bot is connected to.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        Collection<GuildInfo> guilds = ServerManager.getInstance().getGuilds();

        StringBuilder sb = new StringBuilder();
        sb.append("List of guilds:\n");
        for (GuildInfo guild : guilds)
            sb.append(guild.getName()).append(" - ").append(guild.getId()).append("\n");

        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
        return CommandResult.SUCCESS;
    }
}
