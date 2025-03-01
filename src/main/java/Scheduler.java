import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private LinkedList<PCB> queue;
    private Timer timer;
    public PCB currentlyRunning;

    public Scheduler() {
        queue = new LinkedList<>();
        timer = new Timer();
        // Schedule an interrupt every 250 ms.
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (currentlyRunning != null) {
                    currentlyRunning.requestStop();
                }
            }
        }, 250, 250);
    }

    // Creates a new process, adds it to the queue, and if nothing is running, switches to it.
    public int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        PCB pcb = new PCB(up, priority);
        queue.add(pcb);
        if (currentlyRunning == null) {
            SwitchProcess();
        }
        return pcb.pid;
    }

    // Switches the current process: if one is running and not finished, it is re-queued;
    // then the next process is taken from the queue and started.
    public void SwitchProcess() {
        if (currentlyRunning != null && !currentlyRunning.isDone()) {
            queue.add(currentlyRunning);
        }
        if (!queue.isEmpty()) {
            currentlyRunning = queue.removeFirst();
            currentlyRunning.start();
        } else {
            currentlyRunning = null;
        }
    }

}
