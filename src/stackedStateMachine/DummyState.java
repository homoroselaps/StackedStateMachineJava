package stackedStateMachine;

/**
 * Test state which simulates an action which takes some time
 * @author homoroselaps
 *
 */
public class DummyState extends DebugState {
	private int counter;
	public DummyState(int counter) {
		this.counter = counter;
	}
	
	@Override
	public State visitTimerEvent(TimerEvent e) {
		super.visitTimerEvent(e);
		counter--;
		if (counter <= 0) {
			return new DoneState();
		}
		return null;
	}
}
