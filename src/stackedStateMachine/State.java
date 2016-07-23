package stackedStateMachine;
import stackedStateMachine.IEvent;
import java.util.Dictionary;
import java.util.HashMap;

import stackedStateMachine.AbortEvent;

public class State implements IState
{
    public State() {
        addOnActivateHandler(AbortEvent.class, (IEvent e) -> { return onActivate((AbortEvent)e); });
        addOnActivateHandler(DoneEvent.class, (IEvent e) -> { return onActivate((DoneEvent)e); });
    }
    protected interface OnActivateHandler {
    	IEvent run(IEvent e);
    }
    protected interface OnDeactivateHandler {
    	void run(IEvent e);
    }
    HashMap<Class, OnActivateHandler> onActivateHandlers =  new HashMap<Class, OnActivateHandler>();
    HashMap<Class, OnDeactivateHandler> onDeactivateHandlers = new HashMap<Class, OnDeactivateHandler>();
    protected void addOnActivateHandler(Class eventType, OnActivateHandler eventHandler) {
        onActivateHandlers.put(eventType, eventHandler);
    }
    protected void addOnDeactivateHandler(Class eventType, OnDeactivateHandler eventHandler) {
        onDeactivateHandlers.put(eventType, eventHandler);
    }
    public IEvent activateState(IEvent e) {
        if (e != null && onActivateHandlers.containsKey(e.getClass()))
            return onActivateHandlers.get(e.getClass()).run(e);
        else
            return onActivate();
    }
    public void deactivateState(IEvent e) {
        if (e != null && onDeactivateHandlers.containsKey(e.getClass()))
            onDeactivateHandlers.get(e.getClass()).run(e);
        else
            onDeactivate();
    }

    protected IEvent onActivate() { return null; }
    protected IEvent onActivate(AbortEvent e) { return e; }
    protected IEvent onActivate(DoneEvent e) { return null; }
    protected IEvent onDeactivate() { return null; }
    protected void onDeactivate(AbortEvent e) { }
    protected void onDeactivate(DoneEvent e) { }
	
    @Override
	public IEvent onGameTick() {
		return null;
	}
}