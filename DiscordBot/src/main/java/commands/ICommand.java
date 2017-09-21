package commands;

/**
 * The command interface, which helps share similar
 *  methods between each derived objects.
 * @author Tom
 */
public interface ICommand {
    /**
     * Gets the description of the command
     * @return the description.
     */
    String getDescription();

    /**
     * Gets the name of the command.
     * @return the name.
     */
    String getName();

    /**
     * Gets the syntax of the command.
     * @return the syntax.
     */
    String getSyntax();

    /**
     * Hides the command from the help command.
     */
    void hide();

    /**
     * Checks if the command is hidden.
     * @returntrue if the command is hidden, false otherwise.
     */
    boolean isHidden();
}
