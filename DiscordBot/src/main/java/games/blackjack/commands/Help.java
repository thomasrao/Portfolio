package games.blackjack.commands;

import commands.CommandResult;
import games.blackjack.Blackjack;

/**
 * @author Tom
 */
public class Help extends BlackjackCommand {
    public Help() {
        super("help", "[command_name]", "Gives a list of command or some information about a specific command.");
    }

    @Override
    public CommandResult handle(Blackjack game, long memberId, String message) {
        String[] parts = message.split(" ");
        if (parts.length < 2 || !BlackjackCommandManager.getInstance().has(parts[1]))
            game.sendMessage(BlackjackCommandManager.getInstance().toString()).submit();
        else
            game.sendMessage(BlackjackCommandManager.getInstance().get(parts[1]).toString()).submit();
        return CommandResult.SUCCESS;
    }
}
