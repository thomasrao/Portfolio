package commands.client;

import commands.CommandManager;
import commands.CommandResult;
import tools.logging.Log;

/**
 * The command manager for console decides
 *  how every command is stored.
 * It also decides how the help information
 *  is displayed to the chat.
 *
 * @author Tom
 */
public final class ClientCommandManager extends CommandManager<ClientCommand> {
    private final static ClientCommandManager _instance;


    static {
        _instance = new ClientCommandManager();
    }

    private ClientCommandManager() {
        super();

        add(new BotLink());
        add(new Help());
        add(new ListGuilds());
        add(new ListUsers());
        add(new Save());
        add(new SetLevel());
        add(new Test());
    }


    /**
     * Gets the instance of this manager.
     * @return the instance of this command manager.
     */
    public static ClientCommandManager getInstance() {
        return _instance;
    }

    public boolean run(String message, String prefix) {
        if (!message.toLowerCase().startsWith(prefix))
            return false;

        // Get command name.
        message = message.substring(prefix.length());
        int spaceIndex = message.indexOf(' ');
        int endIndex = spaceIndex == -1 ? message.length() : spaceIndex;
        String command = message.substring(0, endIndex);

        // Check if the command exists.
        if (has(command)) {
            // Execute it and get its result.
            CommandResult result = get(command).handle(message);

            // Output & log if required.
            switch (result) {
                case SUCCESS:
                    break;
                case SYNTAX:
                    System.out.println("Invalid syntax for `" + command + "` command.\nCommand usage: " + prefix + command + " " + get(command).getSyntax());
                    break;
                case FAILURE:
                    break;
                default:
                    Log.getInstance().warn("New command result found: " + (result == null ? "null" : result.name()) + ".", "PrivateCommandManager");
                    break;
            }

            return true;
        }
        return false;
    }
}
