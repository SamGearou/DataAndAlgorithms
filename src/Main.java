import concurrency.semaphore.Consumer;
import concurrency.semaphore.Event;
import concurrency.semaphore.Producer;

public class Main {
    public static void main(String[] args) {
        Event q = new Event();
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);
    }
}