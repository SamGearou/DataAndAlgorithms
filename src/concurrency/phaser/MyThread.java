package concurrency.phaser;

import java.util.concurrent.Phaser;

public class MyThread implements Runnable {
    Phaser phsr;
    String name;

    MyThread(Phaser phsr, String name) {
        this.phsr = phsr;
        this.name = name;
        phsr.register();
        new Thread(this).start();
    }

    public void run() {
        System.out.println("Thread " + name + " Beginning Phase One");
        phsr.arriveAndAwaitAdvance(); // Signal arrival

        // pause a bit to prevent jumbled output. This is for illutration purposes
        // only. It is not required for the proper operation of the concurrency.phaser.
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Thread " + name + " Beginning Phase Two");
        phsr.arriveAndAwaitAdvance(); // Signal arrival

        // pause a bit to prevent jumbled output. This is for illutration purposes
        // only. It is not required for the proper operation of the concurrency.phaser.
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Thread " + name + " Beginning Phase Three");
        phsr.arriveAndDeregister(); // Signal arrival and deregister
    }
}
