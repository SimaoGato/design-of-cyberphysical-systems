package exercises.trafficlight;

import com.pi4j.io.gpio.*;

public class TrafficLight {

    private final GpioPinDigitalOutput _green;
    private final GpioPinDigitalOutput _yellow;
    private final GpioPinDigitalOutput _red;

    private final static String GREEN = "Green";
	private final static String YELLOW = "Yellow";
	private final static String RED = "Red";

    public TrafficLight(GpioController gpio, String title, boolean showYellow, com.pi4j.io.gpio.Pin greenPin, com.pi4j.io.gpio.Pin yellowPin, com.pi4j.io.gpio.Pin redPin) {

        _green = gpio.provisionDigitalOutputPin(greenPin, GREEN + title, PinState.LOW);
		if (yellowPin != null) {
			_yellow = gpio.provisionDigitalOutputPin(yellowPin, YELLOW + title, PinState.LOW);
		} else {
			_yellow = null;
		}
		_red = gpio.provisionDigitalOutputPin(redPin, RED + title, PinState.LOW);

		_green.setShutdownOptions(true, PinState.LOW);
		if (_yellow != null)
			_yellow.setShutdownOptions(true, PinState.LOW);
		_red.setShutdownOptions(true, PinState.LOW);
    }

	public void turnOn(GpioPinDigitalOutput pin, boolean on) {
		if (on) {
			pin.high();
		} else {
			pin.low();
		}
	}

    public void showGreen() {
		turnOn(_red, false);
        if (_yellow != null)
			turnOn(_yellow, false);
		turnOn(_green, true);
    }

    public void showRed() {
		turnOn(_red, true);
        if (_yellow != null)
			turnOn(_yellow, false);
		turnOn(_green, false);
    }

    public void showRedYellow() {
        if (_yellow == null) {
            throw new UnsupportedOperationException(
                    "Traffic light has no yellow light.");
        }
		turnOn(_red, true);
		turnOn(_yellow, true);
		turnOn(_green, false);
    }

    public void showYellow() {
        if (_yellow == null) {
            throw new UnsupportedOperationException(
                    "Traffic light has no yellow light.");
        }
		turnOn(_red, false);
		turnOn(_yellow, true);
		turnOn(_green, false);
    }

    public void switchAllOff() {
		turnOn(_red, false);
        if (_yellow != null)
			turnOn(_yellow, false);
		turnOn(_green, false);
    }

}
