public class GoodbyeWorld extends UserlandProcess {
    @Override
    public void main() {
        while (true) {
            System.out.println("Goodbye world");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            cooperate();
        }
    }
}
