package tools.scheduler;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class Reminder extends TimerTask {
    private static final int LATE_DELAY_THRESHOLD = 30000; // Delay before the reminder is considered late.

    private int _id;                  // Unique identifier
    private String _name;             // Name given for this reminder
    private Calendar _date;           // Date & time of reminder
    private Member _member;           // Member who requested this reminder
    private MessageChannel _channel;  // Channel where the reminder was set in


    protected Reminder(int id, String name, Calendar date, Member member, MessageChannel channel) {
        _id = id;
        _name = name;
        _date = date;
        _member = member;
        _channel = channel;
    }


    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        MessageBuilder mb = new MessageBuilder();

        // Creating a message which includes the reminder's name if available.
        mb.append("Alerting for reminder");
        if (_name != null && !_name.isEmpty())
            mb.append(" named \"").append(_name).append("\"");
        mb.append(", ").append(_member.getAsMention());

        // Check if this reminder was late.
        long delay = System.currentTimeMillis() - _date.getTimeInMillis();
        if (delay > LATE_DELAY_THRESHOLD)
            mb.append(" (late by ").append(delay / 1000).append(" seconds)");

        mb.append('.');

        _channel.sendMessage(mb.build()).submit();
    }

    /**
     * Gets the date & time of the reminder.
     * @return the date & time of the reminder
     */
    public Date getDate() {
        return _date.getTime();
    }
}
