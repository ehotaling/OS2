public class PCB { // Process Control Block
    private static int nextPid = 1;
    public int pid;
    private OS.PriorityType priority;
    private UserlandProcess process;
    int timeoutCount;
    long wakeTime; // 0 if not sleeping
    boolean exited;

    PCB(UserlandProcess up, OS.PriorityType priority) {
        this.pid = nextPid++;
        this.priority = priority;
        this.process = up;
        this.timeoutCount = 0;
        this.wakeTime = 0;
        this.exited = false;
        // Launch the userland process after it’s fully constructed.
        process.launch();
    }

    public String getName() {
        return process.getClass().getSimpleName();
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    // Increment timeout count; if more than 5 in a row, demote priority.
    public void incrementTimeout() {
        timeoutCount++;
        if (timeoutCount > 5) {
            if (priority == OS.PriorityType.realtime) {
                priority = OS.PriorityType.interactive;
            } else if (priority == OS.PriorityType.interactive) {
                priority = OS.PriorityType.background;
            }
            timeoutCount = 0; // reset count after demotion
        }
    }

    public void resetTimeout() {
        timeoutCount = 0;
    }

    public void requestStop() {
        process.requestStop();
    }

    public void stop() {
        process.stopProcess();
        while (!process.isStopped()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isDone() {
        return process.isDone();
    }

    // Starts (unblocks) the process.
    void start() {
        process.startProcess();
    }

    public void exit() {
        exited = true;
    }
}
