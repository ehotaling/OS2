public class RealTimeProcess extends UserlandProcess {
    @Override
    public void main() {
        while (true) {
            System.out.println("RealTime Process running.");
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // This process does not call Sleep(), so it gets preempted repeatedly.
            cooperate();
        }
    }
}
