import java.util.ArrayList;
import java.util.List;

public class OS {
    private static Kernel ki; // The one and only instance of the kernel.
    public static List<Object> parameters = new ArrayList<>();
    public static volatile Object retVal;

    public enum CallType {SwitchProcess, SendMessage, Open, Close, Read, Seek, Write, GetMapping, CreateProcess, Sleep, GetPID, AllocateMemory, FreeMemory, GetPIDByName, WaitForMessage, Exit}
    public static volatile CallType currentCall;

    // Called to wake up the kernel to handle a call.
    private static void startTheKernel() {
        // Wait until ki is initialized.
        while (ki == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        ki.wakeUp();
    }

    public static void switchProcess() {
        parameters.clear();
        currentCall = CallType.SwitchProcess;
        startTheKernel();
    }

    // Startup: initialize kernel, then create processes.
    public static void Startup(UserlandProcess init) {
        ki = new Kernel();
        // Launch the kernel thread only after the Kernel is fully constructed.
        ki.launch();
        // Now create initial processes.
        CreateProcess(init, PriorityType.interactive);
        CreateProcess(new IdleProcess(), PriorityType.background);
    }

    public enum PriorityType {realtime, interactive, background}

    public static int CreateProcess(UserlandProcess up, PriorityType priority) {
        parameters.clear();
        parameters.add(up);
        parameters.add(priority);
        currentCall = CallType.CreateProcess;
        startTheKernel();
        // Busy-wait until the kernel call sets retVal.
        while (retVal == null) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        int pid = (int) retVal;
        retVal = null;
        return pid;
    }

    public static int GetPID() {
        parameters.clear();
        currentCall = CallType.GetPID;
        startTheKernel();
        return (int) retVal;
    }

    public static void Exit() {
        parameters.clear();
        currentCall = CallType.Exit;
        startTheKernel();
    }

    public static void Sleep(int mills) {
        parameters.clear();
        parameters.add(mills);
        currentCall = CallType.Sleep;
        startTheKernel();
    }

    // Device-related stubs.
    public static int Open(String s) {
        return 0;
    }

    public static void Close(int id) {
    }

    public static byte[] Read(int id, int size) {
        return null;
    }

    public static void Seek(int id, int to) {
    }

    public static int Write(int id, byte[] data) {
        return 0;
    }

    // Message-related stubs.
    public static void SendMessage(KernelMessage km) {
    }

    public static KernelMessage WaitForMessage() {
        return null;
    }

    public static int GetPidByName(String name) {
        return 0;
    }

    // Memory-related stubs.
    public static void GetMapping(int virtualPage) {
    }

    public static int AllocateMemory(int size) {
        return 0;
    }

    public static boolean FreeMemory(int pointer, int size) {
        return false;
    }
}
