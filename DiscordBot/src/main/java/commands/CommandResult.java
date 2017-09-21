package commands;

/**
 * The result of any given command after
 *  it is executed.
 *
 * @author Tom
 */
public enum CommandResult {
    SUCCESS, // Usually used to show a command's success.
    SYNTAX,  // Used to show the inability to follow the syntax.
    FAILURE  // Some grave error occurred that is abnormal. Or some terrible user syntax for subcommands.
}
