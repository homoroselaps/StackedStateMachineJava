package stackedStateMachine;

/**
 * State used to trigger the rollback of the active state.
 * @author homoroselaps
 * Return this state as nextState to raise an DoneEvent 
 */
public final class DoneState extends State {
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		e.context.raiseEvent(new DoneEvent(e.context));
		return null;
	}
}
