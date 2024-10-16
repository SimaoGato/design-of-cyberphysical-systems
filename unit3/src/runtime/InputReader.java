package runtime;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class InputReader extends Thread {

    private java.util.concurrent.Semaphore sem;
    private Scheduler scheduler;
    private String lastInput;
    private int count = 1;

    public InputReader(Scheduler s) {
        scheduler = s;
        sem = new Semaphore(0);
        lastInput = "";
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                sem.acquire();
                lastInput = Integer.toString(count);
                count++;
                scheduler.addToQueueLast("InputReceived");
                if (count == 100) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestInput() {
        sem.release();
    }

    public String getLastInput() {
        return lastInput;
    }
}
