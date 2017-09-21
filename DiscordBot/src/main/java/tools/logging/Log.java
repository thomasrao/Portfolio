package tools.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Outputs messages with specific prefixes to files.
 *
 * @author Tom
 */
public final class Log {
    private enum LogLevel {
        INFO,    // Regular information
        DEBUG,   // Debugging information
        WARNING, // Information to take note of
        ERROR    // Information regarding errors
    }

    private final static String LOGS_FILE_PATH = "logs";  // The location of the logs
    private final static String LOG_EXTENSION = ".txt";   // The extension of logs
    private final static String LOG_SEPARATOR = "-----------------------------------------------------------------------";
    private final static String LOG_NEW_LINE = System.getProperty("line.separator");

    private final static Log instance; // The sole instance of Log


    static {
        instance = new Log();
    }

    private Log() {}


    private String getShortDateTime() {
        return SimpleDateFormat.getDateTimeInstance().format(new Date());
    }

    /**
     * Writes to a file as debugging information.
     * @param message the message to write.
     * @param fileName the name of the file to write to.
     */
    public void debug(String message, String fileName) {
        write(LogLevel.DEBUG, getShortDateTime() + LOG_NEW_LINE + message + LOG_NEW_LINE + LOG_SEPARATOR + LOG_NEW_LINE, fileName);
    }

    /**
     * Writes to a file as errors.
     * @param message the message to write.
     * @param fileName the name of the file to write to.
     */
    public void error(String message, String fileName) {
        write(LogLevel.ERROR, getShortDateTime() + LOG_NEW_LINE + message + LOG_NEW_LINE + LOG_SEPARATOR + LOG_NEW_LINE, fileName);
    }

    /**
     * Writes the stack trace of a throwable object to a file as errors.
     * @param throwable the exception to write.
     * @param fileName the name of the file to write to.
     */
    public void error(Throwable throwable, String fileName) {
        write(LogLevel.ERROR, throwable, fileName);
    }

    /**
     * Gets the only instance of Log.
     * @return
     */
    public static Log getInstance() {
        return instance;
    }

    /**
     * Writes to a file as regular information.
     * @param message the message to write.
     * @param fileName the name of the file to write to.
     */
    public void info(String message, String fileName) {
        write(LogLevel.INFO, getShortDateTime() + LOG_NEW_LINE + message + LOG_NEW_LINE + LOG_SEPARATOR + LOG_NEW_LINE, fileName);
    }

    /**
     * Writes to a file as warnings.
     * @param message the message to write.
     * @param fileName the name of the file to write to.
     */
    public void warn(String message, String fileName) {
        write(LogLevel.WARNING, getShortDateTime() + LOG_NEW_LINE + message + LOG_NEW_LINE + LOG_SEPARATOR + LOG_NEW_LINE, fileName);
    }

    /**
     * Writes a message to a file.
     * @param message the message to write.
     * @param fileName the name of the file to write to.
     */
    private void write(LogLevel level, String message, String fileName) {
        String filePath = LOGS_FILE_PATH + '/' + level.name() + '/' + fileName + LOG_EXTENSION;

        // Create the log directory if required.
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        // Append to the file.
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.append(message);
        } catch (IOException ioe) {
            // Hopefully, this never happens, but who knows...
            System.err.print("[LOG] [ERROR] ");
            ioe.printStackTrace();
        }
    }

    /**
     * Writes the stack trace of the throwable to a file.
     * @param throwable the stack trace to write.
     * @param fileName the name of the file to write to.
     */
    private void write(LogLevel level, Throwable throwable, String fileName) {
        String filePath = LOGS_FILE_PATH + '/' + level.name() + '/' + fileName + LOG_EXTENSION;

        // Create the log directory if required.
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        // Append to the file.
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.append(getShortDateTime() + LOG_NEW_LINE);
            throwable.printStackTrace(writer);
            writer.append(LOG_NEW_LINE + LOG_SEPARATOR + LOG_NEW_LINE);
        } catch (IOException ioe) {
            // Hopefully, this never happens, but who knows...
            System.err.print("[LOG] [ERROR] ");
            ioe.printStackTrace();
        }
    }
}
