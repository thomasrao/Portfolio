package commands.client;

import commands.CommandResult;

/**
 * This sends the list of commands available
 * or sends information regarding a specific command
 * to the channel.
 *
 * @author Tom
 */
public class Help extends ClientCommand {
    public Help() {
        super("help", "[command_name]", "Gives a list of command or some information about a specific command.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        // Display the help required.
        String[] parts = message.split(" ");
        if (parts.length < 2 || !ClientCommandManager.getInstance().has(parts[1]))
            System.out.println(ClientCommandManager.getInstance().toString());
        else
            System.out.println(ClientCommandManager.getInstance().get(parts[1]).toString());
        return CommandResult.SUCCESS;
    }
}
