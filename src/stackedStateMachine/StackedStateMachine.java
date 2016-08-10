package stackedStateMachine;

import java.util.HashMap;
import java.util.Stack;
import java.util.function.Supplier;

public class StackedStateMachine
{
	private class Key {

		private final Class<? extends State> stateType;
		private final Class<? extends Event> eventType;

		public Key(Class<? extends State> x, Class<? extends Event> y) {
			this.stateType = x;
			this.eventType = y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Key)) return false;
			Key key = (Key) o;
			return stateType.equals(key.stateType) && eventType.equals(key.eventType);
		}

		@Override
		public int hashCode() {
			return (stateType.toString() + "#" +eventType.toString()).hashCode();
		}
	}
	
	private class Transition {
		Supplier<? extends State> sup;
		boolean clearStack;
		public Transition(Supplier<? extends State> sup, boolean clearStack) {
			this.sup = sup;
			this.clearStack = clearStack;
		}
	}
	
	private Stack<State> stateStack = new Stack<State>();
	private HashMap<Key, Transition> transitions = new HashMap<>();
	private Object context;
	public StackedStateMachine(RootState stateStart, Object context) {
		this.context = context;
		stateStack.push(stateStart);
	}
	
	public State getState() 
	{ 
		return stateStack.peek(); 
	}
	
	public void addTransition(Class<? extends State> state1, Class<? extends Event> e, Supplier<LeafState> stateConstructor) {
		transitions.put(new Key(state1, e), new Transition(stateConstructor, false));
	}
	
	public void addRootTransition(Class<? extends State> state1, Class<? extends Event> e, Supplier<RootState> stateConstructor) {
		transitions.put(new Key(state1, e), new Transition(stateConstructor, true));
	}
	
	private Event handleEvent(Event e) {
		State state = stateStack.peek();
		if (state == null)
			//The state machine has no active state anymore
			return null;
		Class<? extends State> stateType = state.getClass();
		Class<? extends Event> eventType = e.getClass();
		if (e instanceof AbortEvent || e instanceof DoneEvent) {
			if (state != null)
				e.deactivate(state, context, new Out<Boolean>(false));
			stateStack.pop();
			State newState = stateStack.peek();
			if (newState != null)
				return e.activate(newState, context, new Out<Boolean>(false));
		}
		else if (existsValidTransition(stateType, eventType)) {
			if (state != null)
				e.deactivate(state, context, new Out<Boolean>(false));
			Transition trans = transitions.get(new Key(stateType, eventType));
			State newState = trans.sup.get();
			if (trans.clearStack)
				stateStack.clear();
			stateStack.push(newState);
			if (newState != null)
				return e.activate(newState, context, new Out<Boolean>(false));
		}
		else {
			return e.recieve(state, context, new Out<Boolean>(false));
		}
		// an event occurred with no valid transition
		return null;
	}
	public void raiseEvent(Event e) {
		while (e != null) {
			e = handleEvent(e);
		}
	}
	
	public boolean existsValidTransition(Class<? extends Event> eventType) {
		State state = stateStack.peek();
		return existsValidTransition(state != null ? state.getClass(): null, eventType);
	}
	public boolean existsValidTransition(Class<? extends State> stateType, Class<? extends Event> eventType) {
		return transitions.containsKey(new Key(stateType, eventType));
	}
}
