package concurrency.notifyandwait;

public class Producer implements Runnable {
    private Event event;

    public Producer(Event event) {
        this.event = event;
        new Thread(this, "notifyAndWait.Producer").start();
    }

    public void run() {
        int i = 0;
        while (true) {
            event.put(i++);
        }
    }
}