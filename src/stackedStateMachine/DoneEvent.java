package stackedStateMachine;

public class DoneEvent extends Event {

	public DoneEvent(StackedStateMachine sender) {
		super(sender);
	}

	@Override
	public State Accept(IStateVisitor state) {
		return state.visitDoneEvent(this);
	}

}
