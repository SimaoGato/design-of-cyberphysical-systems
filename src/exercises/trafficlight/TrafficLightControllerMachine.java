package exercises.trafficlight;

import runtime.EventWindow;
import runtime.IStateMachine;
import runtime.Scheduler;
import runtime.Timer;

public class TrafficLightControllerMachine implements IStateMachine {

	private static final String PEDESTRIAN_BUTTON_PRESSED = "Pedestrian Button";
	private static final String TIMER_1 = "t1";
	private static final String TIMER_2 = "t2";
	private static final String TIMER_3 = "t3";
	private static final String TIMER_4 = "t4";
	private static final String TIMER_5 = "t5";
	private static final String TIMER_6 = "t6";

	public static final String[] EVENTS = { PEDESTRIAN_BUTTON_PRESSED };

	private enum STATES {
		S0, S1, S2, S3, S4, S5
	}

	private Timer t1 = new Timer("t1");
	private Timer t2 = new Timer("t2");
	private Timer t3 = new Timer("t3");
	private Timer t4 = new Timer("t4");
	private Timer t5 = new Timer("t5");
	private Timer t6 = new Timer("t6");

	protected STATES state = STATES.S0;

	private boolean buttonHasBeenPressed = false;
	private boolean timerHasStarted = false;

	private TrafficLight cars = new TrafficLight("Cars", true);
	private TrafficLight pedestrians = new TrafficLight("Pedestrians", false);

	public TrafficLightControllerMachine() {
		// initial transition
		cars.setVisible(true);
		pedestrians.setVisible(true);
		cars.showGreen();
		pedestrians.showRed();
	}

	public int fire(String event, Scheduler scheduler) {
		if (state == STATES.S0) {
			if (event.equals(TIMER_6)) { //if timer6 ends, which way to go?
				if (buttonHasBeenPressed) {	//if button pressed, normal way
					buttonHasBeenPressed = false;
					t6.start(scheduler, 30000); //to ensure every turn lasts 30s exactly
					cars.showYellow();
					state = STATES.S1;
					t1.start(scheduler, 2000);
					return EXECUTE_TRANSITION;
				} else {	//we wait for 30s in state S0
					t6.start(scheduler, 30000);
					state = STATES.S0;
					return EXECUTE_TRANSITION;
				}
			} else if (event.equals(PEDESTRIAN_BUTTON_PRESSED)) {
				buttonHasBeenPressed = true;
				if (!timerHasStarted || event.equals(TIMER_6) ) { // first turn OR n turn t6 done, the "if" mostly entered in the first
					buttonHasBeenPressed = false;				//turn except when the button is pressed right before the timer ends
					t6.start(scheduler, 30000);				//in which case, it can enter this "if"
					cars.showYellow();
					state = STATES.S1;
					t1.start(scheduler, 2000);
					timerHasStarted = true; //to indicate it's not the first turn anymore; it never goes back to false
					return EXECUTE_TRANSITION;
				}
				return EXECUTE_TRANSITION;
			}

		} else if (state == STATES.S1) {
			if (event.equals(TIMER_1)) {
				cars.showRed();
				t2.start(scheduler, 3000);
				state = STATES.S2;
				return EXECUTE_TRANSITION;
			}
		} else if (state == STATES.S2) {
			if (event.equals(TIMER_2)) {
				pedestrians.showGreen();
				t3.start(scheduler, 5000);
				state = STATES.S3;
				return EXECUTE_TRANSITION;
			}
		} else if (state == STATES.S3) {
			if (event.equals(TIMER_3)) {
				pedestrians.showRed();
				t4.start(scheduler, 2000);
				state = STATES.S4;
				return EXECUTE_TRANSITION;
			}
		} else if (state == STATES.S4) {
			if (event.equals(PEDESTRIAN_BUTTON_PRESSED)) {
				buttonHasBeenPressed = true;
			}
			if (event.equals(TIMER_4)) {
				cars.showRedYellow();
				t5.start(scheduler, 1000);
				state = STATES.S5;
				return EXECUTE_TRANSITION;
			}
		} else if (state == STATES.S5) {
			if (event.equals(TIMER_5)) {
				cars.showGreen();
				state = STATES.S0;
				return EXECUTE_TRANSITION;
			}
		}
		return DISCARD_EVENT;
	}

	public static void main(String[] args) {
		IStateMachine stm = new TrafficLightControllerMachine();
		Scheduler s = new Scheduler(stm);

		EventWindow w = new EventWindow(EVENTS, s);
		w.show();

		s.start();
	}

}
