package commands.guildchannel;

import commands.CommandResult;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import tools.logging.Log;
import tools.scheduler.Scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sets a reminder at a specific date & time.
 * A message is sent back upon the registered time & date.
 *
 * @author Tom
 */
public class Reminder extends GuildChannelCommand {
    private final static String DATE_FORMAT_STRING = "MM/dd/yyyy HH:mm"; // Date & time format required.
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING); // Date & time formatter & parser.
    private final static int MILLISECONDS_IN_MINUTE = 60000;


    public Reminder() {
        super("reminder", "<date with '" + DATE_FORMAT_STRING + "' format | minutes>(=name)", "Adds a reminder.", GuildChannelCommandSet.GENERAL, (byte) 0);
    }


    /**
     * Executes upon the request of this command through guild messages received.
     * @param channel the channel from which the message was received in.
     * @param member the member who sent the message.
     * @param message the message itself.
     * @return the result of the command execution.
     */
    public CommandResult handle(Guild guild, MessageChannel channel, Member member, Message message) {
        int spaceIndex = message.getRawContent().indexOf(' ');

        if (spaceIndex < 0)
            return CommandResult.SYNTAX;

        String[] parts = message.getRawContent().substring(spaceIndex + 1).split("=");

        if (parts.length < 1)
            return CommandResult.SYNTAX;

        // Get the name of the reminder.
        String name = null;
        if (parts.length > 1) {
            StringBuilder sb = new StringBuilder(message.getRawContent().length() - spaceIndex - parts[0].length());
            for (int i = 1; i < parts.length; i++)
                sb.append(parts[i]).append('=');
            sb.delete(sb.length() - 1, sb.length());

            name = sb.toString();
        }

        // Get the date of the reminder.
        Date date = null;
        try {
            date = DATE_FORMAT.parse(parts[0]);
        } catch (ParseException pe) {
            Log.getInstance().error(pe.getMessage(), "Reminder");
        }

        Date now = new Date(System.currentTimeMillis());

        // If date is still null, then the input may
        // be number of minutes before the alert happens.
        if (date == null) {
            int minutes;

            try {
                minutes = Integer.parseInt(parts[0]);
            } catch (NumberFormatException nfe) {
                return CommandResult.SYNTAX;
            }

            date = new Date(System.currentTimeMillis() + minutes * MILLISECONDS_IN_MINUTE);
        }

        // Check if the date is in the future.
        if (date.before(now)) {
            channel.sendMessage("The reminder's date & time may not be set to the past, " + member.getAsMention() + ".").submit();
            return CommandResult.FAILURE;
        }

        // Display that the reminder has been set.
        Scheduler.getInstance().addReminder(name, date, member, channel);
        channel.sendMessage("Reminder added, " + member.getAsMention() + ".").submit();
        return CommandResult.SUCCESS;
    }
}
