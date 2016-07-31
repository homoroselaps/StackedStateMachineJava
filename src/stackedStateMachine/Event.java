package stackedStateMachine;

public abstract class Event {
	public StackedStateMachine Sender;
	public Event(StackedStateMachine sender) {
		this.Sender = sender;
	}
	public abstract State Accept(IStateVisitor state);
}
