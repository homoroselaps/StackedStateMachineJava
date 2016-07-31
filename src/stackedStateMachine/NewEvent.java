package stackedStateMachine;

public class NewEvent extends Event {

	public NewEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(IStateVisitor state) {
		return state.visitNewEvent(this);
	}

}
