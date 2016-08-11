package stackedStateMachine;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Supplier;

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
	
	private Stack<State> stateStack = new Stack<State>();
	private HashMap<Key, Supplier<State>> transitions = new HashMap<>();
	private Object context;
	public StackedStateMachine(State stateStart, Object context) {
		this.context = context;
		stateStack.push(stateStart);
		stateStart.activateState(null, context);
	}
	public State getState() 
	{ 
		return stateStack.peek(); 
	}
	public void addTransition(Class state1, Class e, Supplier<State> stateConstructor) {
		transitions.put(new Key(state1, e), stateConstructor);
	}
	
	private Event onActivate(State state, Event e) {
		final MethodType mt = MethodType.methodType(Event.class, Event.class, Object.class);
		Class bal = CarryState.class.getSuperclass();
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findVirtual(state.getClass(), "onActivate", mt);
			return (Event) mh.invoke(state,e,context);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	private void onDeactivate(State state, Event e) {
		final MethodType mt = MethodType.methodType(void.class, Event.class, Object.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findVirtual(state.getClass(), "onDeactivate", mt);
			mh.invoke(state,e,context);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
	
	private Event handleEvent(Event e) {
		System.out.println("handleEvent");
		State state = stateStack.peek();
		if (state == null)
			//The state machine has no active state anymore
			return null;
		Class stateType = state.getClass();
		Class eventType = e.getClass();
		if (e instanceof AbortEvent || e instanceof DoneEvent) {
			if (state != null)
				state.deactivateState(e, context);
			stateStack.pop();
			State newState = stateStack.peek();
			if (newState != null)
				return newState.activateState(e, context);
		}
		else if (existsValidTransition(stateType, eventType)) {
			if (state != null)
				state.deactivateState(e, context);
			Supplier<State> constr = transitions.get(new Key(stateType, eventType));
			State newState = constr.get();
			stateStack.push(newState);
			if (newState != null)
				return newState.activateState(e, context);
		}
		else {
			return state.receive(e, context);
		}
		// an event occured with no valid transition
		return null;
	}
	public void raiseEvent(Event e) {
		while (e != null) {
			e = handleEvent(e);
		}
	}
	
	public boolean existsValidTransition(Class eventType) {
		State state = stateStack.peek();
		return existsValidTransition(state != null ? state.getClass(): null, eventType);
	}
	public boolean existsValidTransition(Class stateType, Class eventType) {
		return transitions.containsKey(new Key(stateType, eventType));
	}
}
