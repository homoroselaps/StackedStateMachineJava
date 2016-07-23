package stackedStateMachine;

import java.util.HashMap;
import java.util.Stack;

public class StackedStateMachine
{
	private class Key {

		private final Class x;
		private final Class y;

		public Key(Class x, Class y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Key)) return false;
			Key key = (Key) o;
			return x.equals(key.x) && y.equals(key.y);
		}

		@Override
		public int hashCode() {
			return (x.toString() + "#" +y.toString()).hashCode();
		}
	}
	
	Stack<State> stateStack = new Stack<State>();
	HashMap<Key, IStateContructor> transitions = new HashMap<>();
	public StackedStateMachine(State stateStart) {
		stateStack.push(stateStart);
		stateStart.activateState(null);
	}
	public IState getState() 
	{ 
		return stateStack.peek(); 
	}
	public void addTransition(Class state1, Class e, IStateContructor stateConstructor) {
		transitions.put(new Key(state1, e), stateConstructor);
	}

	private IEvent handleEvent(IEvent e) {
		State state = stateStack.peek();
		if (state == null)
			//The state machine has no active state anymore
			return null;
		Class stateType = state.getClass();
		Class eventType = e.getClass();
		if (e.getClass().equals(AbortEvent.class)) {
			if (state != null)
				state.deactivateState(new AbortEvent());
			stateStack.pop();
			State newState = stateStack.peek();
			if (newState != null)
				return newState.activateState(new AbortEvent());
		}
		else if (e.getClass().equals(DoneEvent.class)) {
			if (state != null)
				state.deactivateState(new DoneEvent());
			stateStack.pop();
			State newState = stateStack.peek();
			if (newState != null)
				return newState.activateState(new DoneEvent());
		}
		else if (transitions.containsKey(new Key(stateType, eventType))) {
			if (state != null)
				state.deactivateState(e);
			IStateContructor constr = transitions.get(new Key(stateType, eventType));
			State newState = constr.run();
			stateStack.push(newState);
			if (newState != null)
				return newState.activateState(e);
		}
		// an event occured with no valid transition
		return null;
	}
	public void raiseEvent(IEvent e) {
		while (e != null) {
			e = handleEvent(e);
		}
	}
}
