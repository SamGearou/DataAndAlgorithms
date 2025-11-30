package concurrency.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

public class BarDemo {
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(3, new BarAction());

        System.out.println("Starting");

        new concurrency.cyclicbarrier.MyThread(cb, "A");
        new concurrency.cyclicbarrier.MyThread(cb, "B");
        new concurrency.cyclicbarrier.MyThread(cb, "C");
    }
}
