package runtime;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class EventButton {

    GpioPinDigitalInput button;
    private final static String NAME = "Pedestrian Button";

    private final Scheduler _scheduler;

    public EventButton(GpioController gpio, Scheduler scheduler) {
        button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, "Button", PinPullResistance.PULL_DOWN);
        _scheduler = scheduler;

        button.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {
                    _scheduler.addToQueueLast(NAME);
                }
            }
        });
    }
}
