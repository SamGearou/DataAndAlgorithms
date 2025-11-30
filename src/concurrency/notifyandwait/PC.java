package concurrency.notifyandwait;

public class PC {
    public static void main(String[] args) {
        Event event = new Event();
        new Producer(event);
        new Consumer(event);

        System.out.println("Press Control-C to stop.");
    }
}
