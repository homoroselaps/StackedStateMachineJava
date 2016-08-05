package stackedStateMachine;
import stackedStateMachine.Event;
import java.util.HashMap;

import stackedStateMachine.AbortEvent;

public class State
{
	public State() {
		addOnActivateHandler(AbortEvent.class, (Event e) -> { return onActivate((AbortEvent)e); });
		addOnActivateHandler(DoneEvent.class, (Event e) -> { return onActivate((DoneEvent)e); });
	}
	protected interface OnActivateHandler {
		Event run(Event e);
	}
	HashMap<Class, OnActivateHandler> onActivateHandlers =  new HashMap<Class, OnActivateHandler>();
	protected void addOnActivateHandler(Class eventType, OnActivateHandler eventHandler) {
		onActivateHandlers.put(eventType, eventHandler);
	}
	
	protected interface OnDeactivateHandler {
		void run(Event e);
	}
	HashMap<Class, OnDeactivateHandler> onDeactivateHandlers = new HashMap<Class, OnDeactivateHandler>();
	protected void addOnDeactivateHandler(Class eventType, OnDeactivateHandler eventHandler) {
		onDeactivateHandlers.put(eventType, eventHandler);
	}
	
	protected interface OnRecieveHandler {
		Event run(Event e);
	}
	HashMap<Class, OnRecieveHandler> onRecieveHandlers = new HashMap<Class, OnRecieveHandler>();
	protected void addOnRecieveHandler(Class eventType, OnRecieveHandler eventHandler) {
		onRecieveHandlers.put(eventType, eventHandler);
	}
	
	public Event activateState(Event e) {
		if (e != null && onActivateHandlers.containsKey(e.getClass()))
			return onActivateHandlers.get(e.getClass()).run(e);
		else
			return onActivate();
	}
	public void deactivateState(Event e) {
		if (e != null && onDeactivateHandlers.containsKey(e.getClass()))
			onDeactivateHandlers.get(e.getClass()).run(e);
		else
			onDeactivate();
	}
	public Event recieveEvent(Event e) {
		if (e != null && onRecieveHandlers.containsKey(e.getClass()))
			return onRecieveHandlers.get(e.getClass()).run(e);
		else
			return onRecieve();
	}

	protected Event onActivate() { return null; }
	protected Event onActivate(AbortEvent e) { return e; }
	protected Event onActivate(DoneEvent e) { return null; }
	protected Event onDeactivate() { return null; }
	protected void onDeactivate(AbortEvent e) { }
	protected void onDeactivate(DoneEvent e) { }
	protected Event onRecieve() { return null; }
}