package concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CDLDemo {
    public static void main(String[] args) {
        CountDownLatch cdl = new CountDownLatch(5);

        System.out.println("Starting Count Down Latch Demo");

        new MyThread(cdl);

        try {
            cdl.await(); // waits until the cdl reaches 0 before proceeding
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }

        System.out.println("Done");
    }
}
