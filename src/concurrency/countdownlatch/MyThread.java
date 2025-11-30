package concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class MyThread implements Runnable {
    CountDownLatch latch;

    public MyThread(CountDownLatch latch) {
        this.latch = latch;
        new Thread(this).start();
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            latch.countDown(); // decrement count on the count down latch
        }
    }
}
