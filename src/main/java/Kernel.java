public class Kernel extends Process {
    private Scheduler scheduler;

    public Kernel() {
        // Initialize the scheduler.
        scheduler = new Scheduler();
    }

    // A method to wake the kernel process up (i.e. release its semaphore).
    public void wakeUp() {
        startProcess();  // Uses the new startProcess() from Process.
    }

    @Override
    public void main() {
        // The scheduler should be fully initialized by now.
        while (true) { // Infinite loop to process kernel calls.
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
                // Other kernel calls (Sleep, GetPID, etc.) are ignored for assignment 1.
                default:
                    break;
                    /*
                    // Priority Schduler
                    case Sleep -> Sleep((int) OS.parameters.get(0));
                    case GetPID -> OS.retVal = GetPid();
                    case Exit -> Exit();
                    // Devices
                    case Open ->
                    case Close ->
                    case Read ->
                    case Seek ->
                    case Write ->
                    // Messages
                    case GetPIDByName ->
                    case SendMessage ->
                    case WaitForMessage ->
                    // Memory
                    case GetMapping ->
                    case AllocateMemory ->
                    case FreeMemory ->
                     */
                }
            stopProcess();
                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
            }
    }

    private void SwitchProcess() {}

    // For assignment 1, you can ignore the priority. We will use that in assignment 2
    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        return 0; // change this
    }

    private void Sleep(int mills) {
    }

    private void Exit() {
    }

    private int GetPid() {
        return 0; // change this
    }

    private int Open(String s) {
        return 0; // change this
    }

    private void Close(int id) {
    }

    private byte[] Read(int id, int size) {
        return null; // change this
    }

    private void Seek(int id, int to) {
    }

    private int Write(int id, byte[] data) {
        return 0; // change this
    }

    private void SendMessage(/*KernelMessage km*/) {
    }

    private KernelMessage WaitForMessage() {
        return null;
    }

    private int GetPidByName(String name) {
        return 0; // change this
    }

    private void GetMapping(int virtualPage) {
    }

    private int AllocateMemory(int size) {
        return 0; // change this
    }

    private boolean FreeMemory(int pointer, int size) {
        return true;
    }

    private void FreeAllMemory(PCB currentlyRunning) {
    }

}