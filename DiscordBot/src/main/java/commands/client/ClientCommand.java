package commands.client;

import commands.Command;
import commands.CommandResult;

/**
 * This is a command, a way to invoke a set of methods.
 * It describes its name, which will be how it will get
 *  invoked, its description, its syntax and whether
 *  it should be hidden from the help page.
 * This command is designed for console.
 *
 * @author Tom
 */
public abstract class ClientCommand extends Command {
    protected ClientCommand(String command, String syntax, String description) {
        super(command, syntax, description);
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public abstract CommandResult handle(String message);
}
