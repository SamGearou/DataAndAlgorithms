package concurrency.atomic;

public class AtomThread implements Runnable {
    String name;

    AtomThread(String name) {
        this.name = name;
        new Thread(this).start();
    }

    public void run() {
        System.out.println("Starting " + name);

        for (int i = 1; i <= 3; i++) {
            System.out.println(name + " got: " + Shared.ai.getAndSet(i));
        }
    }
}
