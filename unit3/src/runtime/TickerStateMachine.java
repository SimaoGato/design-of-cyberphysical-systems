package runtime;

import mqtt.MQTTclient;
import sensehat.LEDMatrixTicker;
import sensehat.LEDMatrix;

public class TickerStateMachine implements IStateMachine {

    private LEDMatrixTicker ledMatrixTicker;
    private MQTTclient mqttClient;
    private Scheduler scheduler;
    private static final String TIMER_1 = "t1";
    private Timer t1 = new Timer("t1");
    private boolean hasFinished = true;

    public TickerStateMachine() {

    }

    @Override
    public int fire(String event, Scheduler scheduler) {
        switch(event) {
            case "Start":
                mqttClient = createMQTTClient();
                mqttClient.subscribe("sensehat");
                return EXECUTE_TRANSITION;
            case "MQTTMessageReceived":
                if(!hasFinished) {
                    scheduler.addToQueueLast("MQTTMessageReceived");
                    hasFinished = false;
                    return EXECUTE_TRANSITION;
                }
                ledMatrixTicker.StartWriting(mqttClient.getLastMessage());
                return EXECUTE_TRANSITION;
            case TIMER_1:
                ledMatrixTicker.WritingStep();
                return EXECUTE_TRANSITION;
            case "LEDMatrixTickerWait":
                t1.start(scheduler, 100);
                return EXECUTE_TRANSITION;
            case "LEDMatrixTickerFinished":
                hasFinished = true;
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
        String myAddress = "192.168.0.197";

        return new MQTTclient(broker, myAddress, false, scheduler);
    }

    public void setScheduler(Scheduler s) {
        scheduler = s;
    }

    public void setLEDMatrixTicker(LEDMatrixTicker lmt) {
        ledMatrixTicker = lmt;
    }

    public static void main(String args[]) {
        TickerStateMachine tsm = new TickerStateMachine();
        Scheduler scheduler = new Scheduler(tsm);
        tsm.setScheduler(scheduler);
        tsm.setLEDMatrixTicker(new LEDMatrixTicker(scheduler));
        tsm.fire("Start", scheduler);
        scheduler.start();
    }
}
