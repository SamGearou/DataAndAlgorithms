package concurrency.exchanger;

import java.util.concurrent.Exchanger;

public class MakeString implements Runnable {
    Exchanger<String> ex;
    String str;

    public MakeString(Exchanger<String> ex) {
        this.ex = ex;
        str = "";

        new Thread(this).start();
    }

    public void run() {
        char ch = 'A';

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                str += ch++;
            }
            try {
                str = ex.exchange(str); // exchange the passed in String with the String created in the other thread.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
