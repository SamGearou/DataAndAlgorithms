package concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockThread implements Runnable {
    String name;
    ReentrantLock lock;

    public LockThread(ReentrantLock lock, String name) {
        this.lock = lock;
        this.name = name;
        new Thread(this).start();
    }

    public void run() {
        System.out.println("Starting " + name);

        try {
            // First, concurrency.lock count.
            System.out.println(name + " is waiting to concurrency.lock count.");
            lock.lock(); // blocks until the concurrency.lock is available to grab
            System.out.println(name + " is locking count.");

            Shared.count++;
            System.out.println(name + ": " + Shared.count);

            // Now, allow a context switch -- if possible.
            System.out.println(name + " is sleeping.");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(name + " is interrupted.");
        } finally {
            System.out.println(name + " is unlocking count.");
            lock.unlock(); // releases the lcok
        }
    }
}
