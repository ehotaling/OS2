import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {
    private Thread thread;
    private Semaphore semaphore;
    protected volatile boolean stopRequested;

    public Process() {
        semaphore = new Semaphore(0); // Process starts blocked.
        // Do not start the thread here.
    }

    // Explicitly launch the thread; must be called after the process is fully constructed.
    public void launch() {
        thread = new Thread(this);
        thread.start();
    }

    public void requestStop() {
        stopRequested = true;
    }

    public abstract void main();

    public boolean isStopped() {
        return semaphore.availablePermits() == 0;
    }

    public boolean isDone() {
        return thread == null || !thread.isAlive();
    }

    // Release the semaphore to allow the process to run.
    public void startProcess() {
        semaphore.release();
    }

    // Acquire the semaphore to block the process.
    public void stopProcess() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        main();
    }

    public void cooperate() {
        if (stopRequested) {
            stopRequested = false;
            OS.switchProcess();
            stopProcess();
        }
    }
}
