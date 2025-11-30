package concurrency.semaphore;

import java.util.concurrent.Semaphore;

public class Event {
    int n;

    static Semaphore semCon = new Semaphore(0); // zero threads can access a shared resource at any given time
    static Semaphore semProd = new Semaphore(1); // one thread can access a shared resource at any given time

    void get() {
        try {
            semCon.acquire();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Got: " + n);
        semProd.release();
    }

    void put(int n) {
        try {
            semProd.acquire(); // acquires a permit (for the concurrency.semaphore). acquire() blocks until a permit is acquired
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        this.n = n;
        System.out.println("Put: " + n);
        semCon.release(); // releases one permit
    }
}
