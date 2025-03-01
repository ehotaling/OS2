public class Init extends UserlandProcess {
    @Override
    public void main() {
        // Create test processes with different priorities.
        OS.CreateProcess(new HelloWorld(), OS.PriorityType.interactive);
        OS.CreateProcess(new GoodbyeWorld(), OS.PriorityType.background);
        OS.CreateProcess(new RealTimeProcess(), OS.PriorityType.realtime);
        OS.CreateProcess(new SleepTestProcess(), OS.PriorityType.interactive);

        // After setting up the processes, exit Init.
        OS.Exit();
    }
}
