package concurrency.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread implements Runnable {
    CyclicBarrier cb;
    String name;

    public MyThread(CyclicBarrier cb, String name) {
        this.cb = cb;
        this.name = name;
        new Thread(this).start();
    }

    public void run() {
        System.out.println(name);

        try {
            cb.await(); // pauses execution until all the other threads also call await()
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
