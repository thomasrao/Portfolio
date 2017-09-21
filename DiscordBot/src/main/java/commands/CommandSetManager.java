package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The command manager which has grouping abilities
 *  and decides how every command is stored.
 *  It also decides how the help information
 *  is displayed to the chat.
 *
 * @author Tom
 */
public abstract class CommandSetManager<C extends CommandSet<S>, S extends Enum> implements ICommandManager<C> {
    private final static int MAXIMUM_MESSAGE_LENGTH = 2000;
    private final static int COMMAND_INFO_CAPACITY = 100;

    protected Map<String, C> commands;     // A map of all commands available, used for access.
    protected Map<S, List<C>> commandSets; // A map of all commands grouped by Enum values, used for displaying the list of commands.


    protected CommandSetManager() {
        this.commands = new HashMap<>();
        this.commandSets = new HashMap<>();
    }


    /**
     * Adds a new command to the map of available commands.
     * @param command the new command to add.
     */
    public final void add(C command) {
        if (commands.containsKey(command.getName())) {
            commandSets.get(command.getGroup()).remove(command.getName());

            commands.put(command.getName(), command);
            commandSets.get(command.getGroup()).add(command);
        } else {
            commands.put(command.getName(), command);

            if (!commandSets.containsKey(command.getGroup()))
                commandSets.put(command.getGroup(), new ArrayList<>());

            commandSets.get(command.getGroup()).add(command);
        }
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
        StringBuilder com = new StringBuilder(COMMAND_INFO_CAPACITY);

        String format = "```\n";
        int maximumLength = MAXIMUM_MESSAGE_LENGTH - (formatting ? format.length() : 0);

        if (formatting)
            page.append(format);

        // TODO: fix formatting
        for (S set : commandSets.keySet()) {
            com.append('\n').append(set.name()).append(" Commands:\n");
            if (page.length() + com.length() >= maximumLength) {
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

            page.append(com);
            com.delete(0, com.length());

            for (C command : commandSets.get(set)) {
                if (!command.isHidden()) {
                    com.append(command).append('\n');

                    if (page.length() + com.length() >= maximumLength) {
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

                    page.append(com);
                    com.delete(0, com.length());
                }
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
     * @param commandName the command's name to remove.
     */
    public final void remove(String commandName) {
        S set = commands.get(commandName).getGroup();

        commandSets.get(set).remove(commands.get(commandName));
        if (commandSets.get(set).isEmpty())
            commandSets.remove(set);

        commands.remove(commandName);
    }
}
