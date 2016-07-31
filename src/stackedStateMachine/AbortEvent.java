package stackedStateMachine;

public class AbortEvent extends Event {

	public AbortEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(IStateVisitor state) {
		return state.visitAbortEvent(this);
	}
}
