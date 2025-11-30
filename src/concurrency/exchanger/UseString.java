package concurrency.exchanger;

import java.util.concurrent.Exchanger;

public class UseString implements Runnable {
    Exchanger<String> ex;
    String str;

    public UseString(Exchanger<String> ex) {
        this.ex = ex;
        new Thread(this).start();
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            try {
                str = ex.exchange(""); // exchange the passed in String with the String created in the other thread.
                System.out.println("Got: " + str);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
