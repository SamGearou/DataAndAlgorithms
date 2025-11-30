package concurrency.cyclicbarrier;

public class BarAction implements Runnable {
    public void run() {
        System.out.println("Barrier Reached!");
    }
}
