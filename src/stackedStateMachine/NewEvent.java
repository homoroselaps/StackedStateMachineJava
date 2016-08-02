package stackedStateMachine;

public class NewEvent extends Event {

	public NewEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(State state) {
		return state.visitNewEvent(this);
	}

}
