package stackedStateMachine;

/**
 * State used to trigger a rollback of the state stack.
 * @author homoroselaps
 * Return this state as nextState to raise an AbortEvent 
 */
public final class AbortState extends State {
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		e.context.raiseEvent(new AbortEvent(e.context));
		return null;
	}
}
