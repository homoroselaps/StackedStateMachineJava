package stackedStateMachine;

/**
 * Interface definition for all Methods used by the visitor Pattern.
 * @author homoroselaps
 * These methods provide the internal interface for the StackedStateMachine
 */
public abstract class State {
	/**
	 * Handler for the AbortEvent
	 * @param e the Event raised on the state machine
	 * @return an instance of the next state. Return null if no state transition desired 
	 */
	State visitAbortEvent(AbortEvent e) {
		return new AbortState(); 
	}
	
	/**
	 * Handler for the DoneEvent
	 * @param e the Event raised on the state machine
	 * @return an instance of the next state. Return null if no state transition desired 
	 */
	State visitDoneEvent(DoneEvent e) { return null; }
	
	/**
	 * Handler for the NewEvent
	 * This Event is raised on transition to this state.
	 * @param e the Event raised on the state machine
	 * @return an instance of the next state. Return null if no state transition desired 
	 */
	State visitNewEvent(NewEvent e) { return null; }
	
	/**
	 * Handler for the CarryEvent
	 * This Event is raised to initiate the transport of a resource
	 * @param e The Event raised on the state machine
	 * @return An instance of the next state. Return null if no state transition desired 
	 */
	State visitCarryEvent(CarryEvent e) { return null; }
	
	/**
	 * Handle for the TimerEvent
	 * @param e The Event raised on the state machine
	 * @return An instance of the next state. Return null if no state transition desired
	 */
	State visitTimerEvent(TimerEvent e) { return null; }
}
