package tools.scheduler;

public interface ITimerManager {
    boolean isShutdown();
    boolean isTerminated();
    long getActiveTaskCount();
    long getCompletedTaskCount();
    long getTaskCount();
    int getQueuedTasks();
}
