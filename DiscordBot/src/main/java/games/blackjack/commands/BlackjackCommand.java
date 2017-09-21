package games.blackjack.commands;

import commands.Command;
import commands.CommandResult;
import games.blackjack.Blackjack;

/**
 * @author Tom
 */
public abstract class BlackjackCommand extends Command {
    protected BlackjackCommand(String command, String syntax, String description) {
        super(command, syntax, description);
    }

    public abstract CommandResult handle(Blackjack game, long memberId, String message);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("**").append(getName()).append("**: ").append(getDescription());

        if (getSyntax().length() > 0)
            sb.append("\n  Parameters: ").append(getSyntax());
        return sb.toString();
    }
}
