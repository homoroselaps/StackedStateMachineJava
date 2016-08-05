package stackedStateMachine;

public abstract class Event {
	public Object context;
	public Event(Object context) {
		this.context = context;
	}
}