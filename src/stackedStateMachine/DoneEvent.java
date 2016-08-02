package stackedStateMachine;

/**
 * The Done event implementation of the visitor pattern.
 * For internal use only.
 * It ends the execution of the active state, e.g. if the active state finished with the action. 
 * @author homoroselaps
 */
public class DoneEvent extends Event {

	public DoneEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(State state) {
		return state.visitDoneEvent(this);
	}

}
