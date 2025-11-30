package concurrency.notifyandwait;

public class Consumer implements Runnable {
    private Event event;

    public Consumer(Event event) {
        this.event = event;
        new Thread(this, "notifyAndWait.Consumer").start();
    }

    public void run() {
        while (true) {
            event.get();
        }
    }
}