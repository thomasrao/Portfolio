package tools.scheduler;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.*;

public class Scheduler {
    private final static Scheduler _instance;  // The instance of the scheduler

    private HashMap<Integer, Reminder> _tasks; // The map of tasks still on-going
    private Timer _timer;                      // The timer object which schedules
    private Random _random;                    // A random number generator


    static {
        _instance = new Scheduler();
    }

    private Scheduler() {
        _tasks = new HashMap<>();
        _timer = new Timer();
        _random = new Random();
    }


    /**
     * Adds a reminder with a name & a date. The member is tagged when the alert
     *  goes off in the channel it set in.
     * @param name the name of the reminder.
     * @param date the date & time the alert goes off.
     * @param member the member who requested this reminder.
     * @param channel the channel in which it was requested in.
     */
    public void addReminder(String name, Calendar date, Member member, MessageChannel channel) {
        int id = createReminderIdentifier();
        _tasks.put(id, new Reminder(id, name, date, member, channel));

        _timer.schedule(getReminder(id), getReminder(id).getDate());
    }

    public void addReminder(String name, Date date, Member member, MessageChannel channel) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        addReminder(name, calendar, member, channel);
    }

    private int createReminderIdentifier() {
        int identifier = _random.nextInt(Integer.MAX_VALUE);

        while (_tasks.containsKey(identifier))
            identifier = _random.nextInt(Integer.MAX_VALUE);

        return identifier;
    }

    public static Scheduler getInstance() {
        return _instance;
    }

    public Reminder getReminder(int id) {
        return _tasks.get(id);
    }

    public boolean removeReminder(int id) {
        if (_tasks.containsKey(id)) {
            _tasks.remove(id);
            return true;
        }

        return false;
    }
}
