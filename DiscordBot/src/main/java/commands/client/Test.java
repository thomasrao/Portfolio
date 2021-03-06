package commands.client;

import commands.CommandResult;

/**
 * A simple command for testing purposes:
 *  a reply is sent back to test if the bot's
 *  command system is working.
 *
 * @author Tom
 */
public class Test extends ClientCommand {
    public Test() {
        super("test", "", "Testing commands!");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(String message) {
        if (message.contains(" ")) {
            return CommandResult.SYNTAX;
        }

        System.out.println("Success!");
        return CommandResult.SUCCESS;
    }
}
