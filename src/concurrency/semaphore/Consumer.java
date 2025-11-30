package concurrency.semaphore;

public class Consumer implements Runnable {
    Event event;

    public Consumer(Event event) {
        this.event = event;
        new Thread(this, "Consumer").start();
    }

    public void run() {
        for (int i = 0; i < 20; i++) {
            event.get();
        }
    }
}
