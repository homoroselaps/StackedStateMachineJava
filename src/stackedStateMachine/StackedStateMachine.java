package stackedStateMachine;

import java.util.Stack;

/**
 * Core class StackedStateMachine
 * @author homoroselaps
 * 
 */
public class StackedStateMachine
{	
	Stack<State> stateStack = new Stack<State>();
	
	public StackedStateMachine(State newState) {
		stateStack.push(newState);
		performTransition(new NewEvent(this).Accept(newState));
	}
	
	/**
	 * @return the active state of the stacked machine
	 */
	public State getState() 
	{ 
		return stateStack.peek(); 
	}
	
	/**
	 * Perform a state transition to the nextState.
	 * @param nextState It gets pushed onto the stack. 
	 */
	public void performTransition(State nextState) {
		while (nextState != null) {
			stateStack.push(nextState);
			nextState = new NewEvent(this).Accept(nextState);
		}
	}
	
	/**
	 * Raise an Event to manipulate the state machine.
	 * Never raise AbortEvent/DoneEvent externally.
	 * @param e The raised Event
	 */
	public void raiseEvent(Event e) {
		if (e instanceof AbortEvent || e instanceof DoneEvent) {
			// pop the helper Abort/DoneState
			stateStack.pop();
			// dont pop the last state, as the state machine always needs an active state
			if (stateStack.size() <= 1)
				return;
			stateStack.pop();
		}
		State nextState = e.Accept(stateStack.peek());
		performTransition(nextState);
	}
	
	/**
	 * Trigger an abortion of the active action externally
	 */
	public void abort() {
		performTransition(new AbortState());
	}
}
