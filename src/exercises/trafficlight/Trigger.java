package exercises.trafficlight;

import runtime.IStateMachine;
import runtime.Scheduler;
import runtime.Timer;

public class Trigger implements IStateMachine {

	private static final String TIMER_7 = "t7";
	private static final String TIMER_8 = "t8";
	private static final String SIXTY_SECONDS = "SIXTY_SECONDS";

	private enum STATES {
		S0
	}

	private Timer t7 = new Timer("t7");
	private Timer t8 = new Timer("t8");

	protected STATES state = STATES.S0;

	private boolean timerHasStarted = false;

	// List of traffic light schedulers to notify
	private Scheduler schedulerForTrafficLight1;
	private Scheduler schedulerForTrafficLight2;
	private Scheduler schedulerForTrafficLight3;

	public Trigger(Scheduler s1, Scheduler s2, Scheduler s3) {
		this.schedulerForTrafficLight1 = s1;
		this.schedulerForTrafficLight2 = s2;
		this.schedulerForTrafficLight3 = s3;
	}

	public int fire(String event, Scheduler scheduler) {
		if (state == STATES.S0) {
			System.out.println("State S0");
			if (!timerHasStarted) {
				System.out.println("Starting timers for the first time");
				t7.start(scheduler, 30000); // 30000
				t8.start(scheduler, 60000); // 60000
				timerHasStarted = true;
				return EXECUTE_TRANSITION;
			}
			if (event.equals(TIMER_7)) {
				System.out.println("TIMER_7 fired, sending SIXTY_SECONDS to s2 and s3");
				schedulerForTrafficLight2.addToQueueLast(SIXTY_SECONDS);
				schedulerForTrafficLight3.addToQueueLast(SIXTY_SECONDS);
				t7.start(scheduler, 60000); // Restart timer every 60 seconds
				return EXECUTE_TRANSITION;
			}
			if (event.equals(TIMER_8)) {
				System.out.println("TIMER_8 fired, sending SIXTY_SECONDS to s1");
				schedulerForTrafficLight1.addToQueueLast(SIXTY_SECONDS);
				t8.start(scheduler, 60000); // Restart timer every 60 seconds
				return EXECUTE_TRANSITION;
			}
		}
		return DISCARD_EVENT;
	}

}
