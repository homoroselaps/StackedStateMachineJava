package stackedStateMachine;

/**
 * Carry event to trigger transport of a resource 
 * @author homoroselaps
 *
 */
public class TimerEvent extends Event {

	public TimerEvent(StackedStateMachine context) {
		super(context);
	}

	@Override
	public State Accept(State state) {
		return state.visitTimerEvent(this);
	}
}
