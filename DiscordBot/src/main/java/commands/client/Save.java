package commands.client;

import commands.CommandResult;
import core.server.ServerManager;

/**
 * Manually save the data.
 *
 * @author Tom
 */
public class Save extends ClientCommand {
    protected Save() {
        super("save", "", "Forces the client to save all modified data.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        ServerManager.getInstance().save();
        System.out.println("Save complete.");
        return CommandResult.SUCCESS;
    }
}
