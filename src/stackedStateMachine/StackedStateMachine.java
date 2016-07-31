package stackedStateMachine;

import java.util.Stack;

public class StackedStateMachine
{	
	Stack<State> stateStack = new Stack<State>();
	
	public StackedStateMachine(State newState) {
		stateStack.push(newState);
		handleTransition(new NewEvent(this).Accept(newState));
	}
	public IState getState() 
	{ 
		return stateStack.peek(); 
	}
	
	public void handleTransition(State nextState) {
		while (nextState != null) {
			stateStack.push(nextState);
			nextState = new NewEvent(this).Accept(nextState);
		}
	}
	
	public void raiseEvent(Event e) {
		if (e instanceof AbortEvent || e instanceof DoneEvent) {
			// pop the helper Abort/DoneState
			stateStack.pop();
			// dont pop the last state
			if (stateStack.size() <= 1)
				return;
			stateStack.pop();
		}
		State state = stateStack.peek();
		handleTransition(e.Accept(state));
	}
	public void abort() {
		handleTransition(new AbortState());
	}
}
