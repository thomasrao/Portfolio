package commands;

/**
 * This is a command, a way to invoke a set of methods.
 * It describes its name, which will be how it will get
 *  invoked, its description, its syntax and whether
 *  it should be hidden from the public. In addition,
 *  this command will be grouped in sets when listed.
 *
 * @author Tom
 */
public class CommandSet<S extends Enum> implements ICommand {
    private final String command;      // The command's name (i.e.: how to access it)
    private final String syntax;       // The command's usage/syntax.
    private final String description;  // A description fitting this command.
    private final S set;               // The set it is part of.
    private boolean hidden;            // A flag for visibility.


    protected CommandSet(String command, String syntax, String description, S set) {
        this.command = command.toLowerCase();
        this.syntax = syntax;
        this.description = description;
        this.set = set;
        this.hidden = false;
    }


    /**
     * Gets the description of the command
     * @return the description.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Gets the group that this command is part of.
     * @return the group that this command is part of.
     */
    public final S getGroup() {
        return set;
    }

    /**
     * Gets the name of the command.
     * @return the name.
     */
    public final String getName() {
        return command;
    }

    /**
     * Gets the syntax of the command.
     * @return the syntax.
     */
    public final String getSyntax() {
        return syntax;
    }

    /**
     * Hides the command from the help command.
     */
    public final void hide() {
        hidden = true;
    }

    /**
     * Checks if the command is hidden.
     * @return true if the command is hidden, false otherwise.
     */
    public final boolean isHidden() {
        return hidden;
    }

    /**
     * Gets information about the command, including its syntax.
     * @return information about the command.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(": ").append(getDescription());

        if (getSyntax().length() > 0)
            sb.append("\n  Parameters: ").append(getSyntax());
        return sb.toString();
    }
}
