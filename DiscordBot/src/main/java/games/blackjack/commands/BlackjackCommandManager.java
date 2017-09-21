package games.blackjack.commands;

import commands.CommandManager;
import commands.CommandResult;
import games.blackjack.Blackjack;
import tools.logging.Log;

import java.util.Map;

/**
 * @author Tom
 */
public final class BlackjackCommandManager extends CommandManager<BlackjackCommand> {
    public final static String BLACKJACK_COMMAND_PREFIX = "+";

    private final static BlackjackCommandManager instance; // The singleton instance.


    static {
        instance = new BlackjackCommandManager();
    }

    private BlackjackCommandManager() {
        super();

        add(new Help());
        add(new Hit());
        add(new Stand());
    }


    /**
     * Gets the singleton instance.
     * @return the instance.
     */
    public static BlackjackCommandManager getInstance() {
        return instance;
    }

    /**
     * Runs the given command name if it exists.
     * @param game the blackjack game where the command runs on.
     * @param memberId the member's id who ran the command.
     * @param message the command and its arguments.
     * @param prefix the prefix used.
     * @return true if the command ran successfully, false otherwise.
     */
    public boolean run(Blackjack game, long memberId, String message, String prefix) {
        if (!message.toLowerCase().startsWith(prefix))
            return false;

        message = message.substring(prefix.length());
        int spaceIndex = message.indexOf(' ');
        int endIndex = spaceIndex == -1 ? message.length() : spaceIndex;
        String commandName = message.substring(0, endIndex);

        if (has(commandName)) {
            BlackjackCommand command = get(commandName);
            CommandResult result = command.handle(game, memberId, message);

            switch (result) {
                case SUCCESS:
                    break;
                case SYNTAX:
                    System.out.println("Invalid syntax for `" + commandName + "` command.\nCommand usage: " + prefix + commandName + " " + command.getSyntax());
                    break;
                case FAILURE:
                    break;
                default:
                    Log.getInstance().warn("New command result found: " + (result == null ? "null" : result.name()) + ".", "BlackjackCommandManager");
                    return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Displays information regarding every command available.
     * @return the information about every command available.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, BlackjackCommand> command : commands.entrySet()) {
            if (!command.getValue().isHidden())
                sb.append(BLACKJACK_COMMAND_PREFIX + command.getValue()).append("\n");
        }

        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);

        sb.append("\n\n");
        sb.append("**Note**: Owners bypass several restrictions.");
        return sb.toString();
    }
}
