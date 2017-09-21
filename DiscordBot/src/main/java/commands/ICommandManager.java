package commands;

/**
 * @author Tom
 */
public interface ICommandManager<C extends ICommand> {
    /**
     * Adds a new command to the map of available commands.
     * @param command the new command to add.
     */
    void add(C command);

    /**
     * Gets the command based on the name.
     * @param commandName the command's name to search
     * @return the command searched for, null if not found.
     */
    C get(String commandName);

    /**
     * Checks if there is a command with a given name.
     * @param commandName the name of the command
     * @return true if the command exists, false otherwise.
     */
    boolean has(String commandName);

    /**
     * Removes a command from the map of available commands.
     * @param commandName the command's name to remove.
     */
    void remove(String commandName);
}
