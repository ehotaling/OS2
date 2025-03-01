import java.time.Clock;
import java.util.LinkedList;
import java.util.Random;

public class Scheduler {
    private LinkedList<PCB> queueRealtime;
    private LinkedList<PCB> queueInteractive;
    private LinkedList<PCB> queueBackground;
    private LinkedList<PCB> sleepQueue;
    private Random random;
    public PCB currentlyRunning;

    public Scheduler() {
        queueRealtime = new LinkedList<>();
        queueInteractive = new LinkedList<>();
        queueBackground = new LinkedList<>();
        sleepQueue = new LinkedList<>();
        random = new Random();
    }

    // Create a PCB and add it to the appropriate queue.
    public int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        PCB pcb = new PCB(up, priority);
        addProcessToQueue(pcb);
        if (currentlyRunning == null) {
            SwitchProcess();
        }
        return pcb.pid;
    }

    // Place PCB into the queue based on its current priority.
    private void addProcessToQueue(PCB pcb) {
        switch (pcb.getPriority()) {
            case realtime:
                queueRealtime.add(pcb);
                break;
            case interactive:
                queueInteractive.add(pcb);
                break;
            case background:
                queueBackground.add(pcb);
                break;
        }
    }

    // Check the sleep queue and wake any processes whose wake time has passed.
    private void wakeSleepingProcesses() {
        long now = Clock.systemUTC().millis();
        LinkedList<PCB> toWake = new LinkedList<>();
        for (PCB pcb : sleepQueue) {
            if (pcb.wakeTime <= now) {
                toWake.add(pcb);
            }
        }
        for (PCB pcb : toWake) {
            sleepQueue.remove(pcb);
            pcb.wakeTime = 0;
            addProcessToQueue(pcb);
        }
    }

    // Use probabilistic selection to choose the next process to run.
    private PCB selectNextProcess() {
        wakeSleepingProcesses();
        boolean hasRealtime = !queueRealtime.isEmpty();
        boolean hasInteractive = !queueInteractive.isEmpty();
        boolean hasBackground = !queueBackground.isEmpty();

        if (!hasRealtime && !hasInteractive && !hasBackground) {
            return null;
        }

        PCB selected = null;
        if (hasRealtime) {
            int p = random.nextInt(10) + 1; // 1 to 10
            if (p <= 6 && hasRealtime) {
                selected = queueRealtime.poll();
            } else if (p <= 9 && hasInteractive) {
                selected = queueInteractive.poll();
            } else if (hasBackground) {
                selected = queueBackground.poll();
            } else if (hasRealtime) {
                selected = queueRealtime.poll();
            } else if (hasInteractive) {
                selected = queueInteractive.poll();
            }
        } else if (hasInteractive) {
            int p = random.nextInt(4) + 1; // 1 to 4
            if (p <= 3 && hasInteractive) {
                selected = queueInteractive.poll();
            } else if (hasBackground) {
                selected = queueBackground.poll();
            } else {
                selected = queueInteractive.poll();
            }
        } else {
            selected = queueBackground.poll();
        }
        return selected;
    }

    // Switch the currently running process.
    public void SwitchProcess() {
        // If there is a process running and it hasn't exited, update its timeout count and requeue it.
        if (currentlyRunning != null && !currentlyRunning.isDone() && !currentlyRunning.exited) {
            currentlyRunning.incrementTimeout();
            addProcessToQueue(currentlyRunning);
        }
        currentlyRunning = selectNextProcess();
        if (currentlyRunning != null) {
            currentlyRunning.resetTimeout();
            currentlyRunning.start();
        }
    }

    // Return the PID of the currently running process.
    public int getPid() {
        return currentlyRunning != null ? currentlyRunning.pid : -1;
    }

    // Exit the currently running process.
    public void exitProcess() {
        if (currentlyRunning != null) {
            currentlyRunning.exit();
        }
        SwitchProcess();
    }

    // Put the currently running process to sleep.
    public void sleepProcess(int mills) {
        if (currentlyRunning != null) {
            currentlyRunning.wakeTime = Clock.systemUTC().millis() + mills;
            sleepQueue.add(currentlyRunning);
        }
        SwitchProcess();
    }
}
