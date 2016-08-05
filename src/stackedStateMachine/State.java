package stackedStateMachine;
import stackedStateMachine.Event;
import java.util.HashMap;

import stackedStateMachine.AbortEvent;

public class State
{
	public State() {
		addOnActivateHandler(AbortEvent.class, (Event e, Object context) -> { return onActivate((AbortEvent)e, context); });
		addOnActivateHandler(DoneEvent.class, (Event e, Object context) -> { return onActivate((DoneEvent)e, context); });
	}
	protected interface OnActivateHandler {
		Event run(Event e, Object context);
	}
	HashMap<Class, OnActivateHandler> onActivateHandlers =  new HashMap<Class, OnActivateHandler>();
	protected void addOnActivateHandler(Class eventType, OnActivateHandler eventHandler) {
		onActivateHandlers.put(eventType, eventHandler);
	}
	
	protected interface OnDeactivateHandler {
		void run(Event e, Object context);
	}
	HashMap<Class, OnDeactivateHandler> onDeactivateHandlers = new HashMap<Class, OnDeactivateHandler>();
	protected void addOnDeactivateHandler(Class eventType, OnDeactivateHandler eventHandler) {
		onDeactivateHandlers.put(eventType, eventHandler);
	}
	
	protected interface OnRecieveHandler {
		Event run(Event e, Object context);
	}
	HashMap<Class, OnRecieveHandler> onRecieveHandlers = new HashMap<Class, OnRecieveHandler>();
	protected void addOnRecieveHandler(Class eventType, OnRecieveHandler eventHandler) {
		onRecieveHandlers.put(eventType, eventHandler);
	}
	
	public Event activateState(Event e, Object context) {
		if (e != null && onActivateHandlers.containsKey(e.getClass()))
			return onActivateHandlers.get(e.getClass()).run(e, context);
		else
			return onActivate(context);
	}
	public void deactivateState(Event e, Object context) {
		if (e != null && onDeactivateHandlers.containsKey(e.getClass()))
			onDeactivateHandlers.get(e.getClass()).run(e, context);
		else
			onDeactivate(context);
	}
	public Event recieveEvent(Event e, Object context) {
		if (e != null && onRecieveHandlers.containsKey(e.getClass()))
			return onRecieveHandlers.get(e.getClass()).run(e, context);
		else
			return onRecieve(context);
	}

	protected Event onActivate(Object context) { return null; }
	protected Event onActivate(AbortEvent e, Object context) { return e; }
	protected Event onActivate(DoneEvent e, Object context) { return null; }
	
	protected Event onDeactivate(Object context) { return null; }
	protected void onDeactivate(AbortEvent e, Object context) { }
	protected void onDeactivate(DoneEvent e, Object context) { }
	
	protected Event onRecieve(Object context) { return null; }
}