package runtime;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

import runtime.Scheduler;

public class InputReader extends Thread {

    private java.util.concurrent.Semaphore sem;
    private Scheduler scheduler;
    private String lastInput;

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
                System.out.println("Enter a message: ");
                lastInput = scanner.nextLine();
                scheduler.addToQueueLast("InputReceived");
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
