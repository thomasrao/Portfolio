package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base of every command manager decides
 *  how every command is stored.
 * It also decides how the help information
 *  is displayed to the chat.
 *
 * @author Tom
 */
public abstract class CommandManager<C extends ICommand> implements ICommandManager<C> {
    private final static int MAXIMUM_MESSAGE_LENGTH = 2000;  // The maximum message length.
    private final static int COMMAND_INFO_CAPACITY = 100;    // The default capacity for command info.

    public static final String DEFAULT_COMMAND_PREFIX = "/"; // The default prefix for new guilds.
    public static final byte DEFAULT_COMMAND_LEVEL = 0;      // The default level set for new members.

    protected Map<String, C> commands;                       // A map of all commands available.


    protected CommandManager() {
        this.commands = new HashMap<>();
    }


    /**
     * Adds a new command to the map of available commands.
     * @param command the new command to add.
     */
    public final void add(C command) {
        commands.put(command.getName(), command);
    }

    /**
     * Gets the command based on the name.
     * @param commandName the command's name to search
     * @return the command searched for, null if not found.
     */
    public final C get(String commandName) {
        return commands.get(commandName.toLowerCase());
    }

    /**
     * Displays information regarding every command available
     *  in a list of Strings.
     * @return the information about every command available.
     */
    public List<String> getHelpPages(boolean formatting) {
        List<String> helps = new ArrayList<>();

        StringBuilder page = new StringBuilder(MAXIMUM_MESSAGE_LENGTH);
        StringBuilder commandInfo = new StringBuilder(COMMAND_INFO_CAPACITY);

        String format = "```\n";
        int maximumLength = MAXIMUM_MESSAGE_LENGTH - (formatting ? format.length() : 0);

        if (formatting)
            page.append(format);

        page.append(commandInfo);
        commandInfo.delete(0, commandInfo.length());

        // TODO: fix formatting
        for (C command : commands.values()) {
            if (!command.isHidden()) {
                commandInfo.append(command).append('\n');

                if (page.length() + commandInfo.length() >= maximumLength) {
                    if (formatting) {
                        page.append(format);
                        helps.add(page.toString());
                        page.delete(0, MAXIMUM_MESSAGE_LENGTH);
                        page.append(format);
                    } else {
                        helps.add(page.toString());
                        page.delete(0, MAXIMUM_MESSAGE_LENGTH);
                    }
                }

                page.append(commandInfo);
                commandInfo.delete(0, commandInfo.length());
            }
        }

        if (formatting)
            page.append(format);

        helps.add(page.toString());
        return helps;
    }

    /**
     * Checks if there is a command with a given name.
     * @param commandName the name of the command
     * @return true if the command exists, false otherwise.
     */
    public final boolean has(String commandName) {
        return commands.containsKey(commandName.toLowerCase());
    }

    /**
     * Removes a command from the map of available commands.
     * @param commandName
     */
    public final void remove(String commandName) {
        commands.remove(commandName);
    }

    /**
     * Displays information regarding every command available.
     * @return the information about every command available.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (C command : commands.values()) {
            if (!command.isHidden())
                sb.append(command).append("\n");
        }

        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
