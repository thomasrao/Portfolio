package commands.client;

import commands.CommandResult;

/**
 * This sends the link for the bot's invite link
 *  to the console.
 *
 * @author Tom
 */
public class BotLink extends ClientCommand {
    protected BotLink() {
        super("botlink", "", "Provides the link to addMember this bot to the core.");
    }


    /**
     * Executes upon the request of this command through private messages received.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    @Override
    public CommandResult handle(String message) {
        System.out.println("URL: https://discordapp.com/oauth2/authorize?&client_id=318871492953636864&scope=bot&permissions=8");
        return CommandResult.SUCCESS;
    }
}
