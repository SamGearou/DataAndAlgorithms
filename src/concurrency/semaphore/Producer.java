package concurrency.semaphore;

public class Producer implements Runnable {
    Event event;

    public Producer(Event event) {
        this.event = event;
        new Thread(this, "Producter").start();
    }

    public void run() {
        for (int i = 0; i < 20; i++) {
            event.put(i);
        }
    }
}
