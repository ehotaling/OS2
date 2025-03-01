public class SleepTestProcess extends UserlandProcess {
    @Override
    public void main() {
        while (true) {
            System.out.println("SleepTestProcess: Going to sleep for 1000 ms.");
            OS.Sleep(1000);
            System.out.println("SleepTestProcess: Woke up!");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            cooperate();
        }
    }
}
