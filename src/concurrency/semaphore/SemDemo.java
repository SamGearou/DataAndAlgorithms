package concurrency.semaphore;

public class SemDemo {
    public static void main(String[] args) {
        Event event = new Event();
        new Consumer(event);
        new Producer(event);
    }
}
