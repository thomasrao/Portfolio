package tools.scheduler;

import tools.logging.Log;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerManager implements ITimerManager {
    private static final int THREAD_COUNT = 4;
    private static TimerManager _instance = new TimerManager();

    private ScheduledThreadPoolExecutor _session;


    private TimerManager() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();

        try {
            server.registerMBean(this, new ObjectName("server:type=TimerManger"));
        } catch (Exception e) {
            Log.getInstance().error(e, "TimerManager");
        }
    }


    public static TimerManager getInstance() {
        return _instance;
    }

    public void start() {
        if (_session != null && !_session.isShutdown() && !_session.isTerminated())
            return;

        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(THREAD_COUNT, new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread();
                t.setName("TimerManager-Worker-" + threadNumber.getAndIncrement());
                return t;
            }
        });

        _session = stpe;
    }

    public void stop() {
        _session.shutdownNow();
    }

    public Runnable purge() {
        return new Runnable() {
            public void run() {
                _session.purge();
            }
        };
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime, long delayTime) {
        return _session.scheduleAtFixedRate(new LoggingSaveRunnable(r), delayTime, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime) {
        return _session.scheduleAtFixedRate(new LoggingSaveRunnable(r), 0, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        return _session.schedule(new LoggingSaveRunnable(r), delay, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable r, long timestamp) {
        return schedule(r, timestamp - System.currentTimeMillis());
    }

    private static class LoggingSaveRunnable implements Runnable {
        Runnable r;

        public LoggingSaveRunnable(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            try {
                r.run();
            } catch (Throwable t) {
                Log.getInstance().error(t, "TimerManager");
            }
        }
    }

    @Override
    public long getActiveTaskCount() {
        return _session.getActiveCount();
    }

    public long getCompletedTaskCount() {
        return _session.getCompletedTaskCount();
    }

    public int getQueuedTasks() {
        return _session.getQueue().toArray().length;
    }

    public long getTaskCount() {
        return _session.getTaskCount();
    }

    public boolean isShutdown() {
        return _session.isShutdown();
    }

    public boolean isTerminated() {
        return _session.isTerminated();
    }
}
