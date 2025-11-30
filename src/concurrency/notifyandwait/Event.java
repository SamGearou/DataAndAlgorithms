package concurrency.notifyandwait;

public class Event {
    int n;
    boolean valuesSet = false;

    synchronized int get() {
        while (!valuesSet) {
            try {
                wait(); // tells the calling thread to give up the monitor and go to sleep until some other thread enters the same monitor and calls notify()
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
        System.out.println("Got: " + n);
        valuesSet = false;
        notify();
        return n;
    }

    synchronized void put(int n) {
        while (valuesSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
        this.n = n;
        valuesSet = true;
        System.out.println("Put: " + n);
        notify(); // wakes up a thread that called wait() on hte same object
    }
}
