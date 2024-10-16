package runtime;

import mqtt.MQTTclient;
import sensehat.LEDMatrixTicker;
import java.util.Scanner;

public class Publisher implements IStateMachine {

    private LEDMatrixTicker ledMatrixTicker;
    private MQTTclient mqttClient;
    private Scheduler scheduler;
    private InputReader inputReader;
    private static final String TIMER_1 = "t1";
    private Timer t1 = new Timer("t1");
    private Freepool freepool;

    public Publisher() {

    }

    @Override
    public int fire(String event, Scheduler scheduler) {
        switch(event) {
            case "Start":
                mqttClient = createMQTTClient();
                mqttClient.subscribe("hatsense");
                freepool = new Freepool(6, mqttClient);

                inputReader = new InputReader(scheduler);
                inputReader.start();

                inputReader.requestInput();
                return EXECUTE_TRANSITION;
            case "InputReceived":
                String userInput = inputReader.getLastInput();
                freepool.sendData(userInput, "sensehat");
                // mqttClient.sendMessage("sensehat", userInput);
                if (freepool.getFreepools() > 0) {
                    inputReader.requestInput();
                }
                return EXECUTE_TRANSITION;
            case "ReceivedFreepool":
                inputReader.requestInput();
                return EXECUTE_TRANSITION;
            case "MQTTMessageReceived":
                freepool.receiveFreepool("sensehat");
                scheduler.addToQueueLast("ReceivedFreepool");
                // ledMatrixTicker.StartWriting(mqttClient.getLastMessage());
                return EXECUTE_TRANSITION;
            case TIMER_1:
                ledMatrixTicker.WritingStep();
                return EXECUTE_TRANSITION;
            case "LEDMatrixTickerWait":
                t1.start(scheduler, 100);
                return EXECUTE_TRANSITION;
            case "LEDMatrixTickerFinished":
                System.out.println("LEDMatrixTicker finished");
                return EXECUTE_TRANSITION;
            case "LEDMatrixError":
                System.out.println("LEDMatrixError");
                return DISCARD_EVENT;
            default:
                return DISCARD_EVENT;
        }
    }

    private MQTTclient createMQTTClient() {
        String broker = "tcp://broker.hivemq.com:1883";
        String myAddress = "192.168.0.193";

        return new MQTTclient(broker, myAddress, false, scheduler);
    }

    public void setScheduler(Scheduler s) {
        scheduler = s;
    }


    public static void main(String args[]) {
        Publisher tsm = new Publisher();
        Scheduler scheduler = new Scheduler(tsm);
        tsm.setScheduler(scheduler);
        tsm.fire("Start", scheduler);
        scheduler.start();
    }
}
