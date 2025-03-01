public class Kernel extends Process {
    private Scheduler scheduler;

    public Kernel() {
        scheduler = new Scheduler();
    }

    // Wake up the kernel by releasing its semaphore.
    public void wakeUp() {
        startProcess();
    }

    @Override
    public void main() {
        while (true) {
            switch (OS.currentCall) {
                case CreateProcess:
                    OS.retVal = scheduler.CreateProcess(
                            (UserlandProcess) OS.parameters.get(0),
                            (OS.PriorityType) OS.parameters.get(1)
                    );
                    break;
                case SwitchProcess:
                    scheduler.SwitchProcess();
                    break;
                case Sleep:
                    int mills = (int) OS.parameters.get(0);
                    scheduler.sleepProcess(mills);
                    break;
                case GetPID:
                    OS.retVal = scheduler.getPid();
                    break;
                case Exit:
                    scheduler.exitProcess();
                    break;
                default:
                    break;
            }
            // Block until the next kernel call.
            stopProcess();
        }
    }
}
