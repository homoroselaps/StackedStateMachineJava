package stackedStateMachine;

/**
 * The Abort event implementation of the visitor pattern.
 * For internal use only.
 * It initiates a rollback of the state stack, e.g. if the active state runs into a problem. 
 * @author homoroselaps
 * To trigger an rollback of the state stack, use the functionality of the state machine.
 */
public class AbortEvent extends Event {
	
	public AbortEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(State state) {
		return state.visitAbortEvent(this);
	}
}
