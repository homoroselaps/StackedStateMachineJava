package stackedStateMachine;
import stackedStateMachine.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import stackedStateMachine.AbortEvent;

public abstract class State
{
	protected MethodLookup onActivateHandles = new MethodLookup();
	protected MethodLookup onDeactivateHandles = new MethodLookup();
	protected MethodLookup onRecieveHandles = new MethodLookup();
	public State() {
		addOnActivateHandler(AbortEvent.class);
		addOnActivateHandler(DoneEvent.class);
		addOnActivateHandler(Event.class);
		
		addOnDeactivateHandler(AbortEvent.class);
		addOnDeactivateHandler(DoneEvent.class);
		addOnDeactivateHandler(Event.class);
		
		addOnRecieveHandler(Event.class);
	}
	
	public void addOnActivateHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(Event.class, eventClass, Object.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findVirtual(this.getClass(), "onActivate", mt);
			onActivateHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}
	}
	
	public void addOnDeactivateHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(void.class, eventClass, Object.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findVirtual(this.getClass(), "onDeactivate", mt);
			onDeactivateHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}
	}
	
	public void addOnRecieveHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(Event.class, eventClass, Object.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findVirtual(this.getClass(), "onRecieve", mt);
			onRecieveHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}
	}
	
	public Event activateState(Event e, Object context) {
		if (e != null) {
			try {
				MethodHandle mh = onActivateHandles.get(e.getClass());
				return (Event) mh.invoke(this, e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public Event recieve(Event e, Object context) {
		if (e != null) {
			try {
				MethodHandle mh = onRecieveHandles.get(e.getClass());
				return (Event) mh.invoke(this, e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public void deactivateState(Event e, Object context) {
		if (e != null) {
			try {
				MethodHandle mh = onActivateHandles.get(e.getClass());
				mh.invoke(this, e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	public Event onActivate(Event e, Object context) { return null; }
	public abstract Event onActivate(AbortEvent e, Object context);
	public abstract Event onActivate(DoneEvent e, Object context);
	
	public void onDeactivate(Event e, Object context) {}
	public abstract void onDeactivate(AbortEvent e, Object context);
	public abstract void onDeactivate(DoneEvent e, Object context);
	
	public Event onRecieve(Event e, Object context) { return null; }
}