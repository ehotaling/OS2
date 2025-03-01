public class PCB { // Process Control Block
    private static int nextPid = 1;
    public int pid;
    private OS.PriorityType priority;
    private UserlandProcess process;

    PCB(UserlandProcess up, OS.PriorityType priority) {
        this.pid = nextPid++;
        this.priority = priority;
        this.process = up;
        // Launch the userland process after it’s fully constructed.
        process.launch();
    }

    public String getName() {
        return process.getClass().getSimpleName();
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
        process.requestStop();
    }

    public void stop() {
        process.stopProcess();
        // Wait until the process has actually blocked.
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

    // Starts the userland process.
    void start() {
        process.startProcess();
    }

    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }
}
