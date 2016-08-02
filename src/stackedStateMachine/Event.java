package stackedStateMachine;

/**
 * Event base class. Event communicate details, and provide access to the controlled object.
 * @author homoroselaps
 */
public abstract class Event {
	/**
	 * The Sender of the Event.
	 */
	public StackedStateMachine context;
	
	public Event(StackedStateMachine context) {
		this.context = context;
	}
	
	/**
	 * Visitor Pattern accept method.
	 * @param state The state visiting the event.
	 * @return The next state. If no transition desired return null. 
	 */
	public abstract State Accept(State state);
}
