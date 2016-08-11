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
	protected MethodLookup onReceiveHandles = new MethodLookup();
	public State() {
		addOnActivateHandler(AbortEvent.class);
		addOnActivateHandler(DoneEvent.class);
		addOnActivateHandler(Event.class);
		
		addOnDeactivateHandler(AbortEvent.class);
		addOnDeactivateHandler(DoneEvent.class);
		addOnDeactivateHandler(Event.class);
		
		addOnRecieveHandler(Event.class);
	}
	
	public StateContext buildContext(StateContext context) {
		return context;
	}
	
	public void addOnActivateHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(Event.class, eventClass, StateContext.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findStatic(this.getClass(), "onActivate", mt);
			onActivateHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}		
	}
	
	public void addOnDeactivateHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(void.class, eventClass, StateContext.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findStatic(this.getClass(), "onDeactivate", mt);
			onDeactivateHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}
	}
	
	public void addOnRecieveHandler(Class<? extends Event> eventClass) {
		final MethodType mt = MethodType.methodType(Event.class, eventClass, StateContext.class);
		MethodHandle mh = null;
		try {
			mh = MethodHandles.lookup().findStatic(this.getClass(), "onReceive", mt);
			onReceiveHandles.add(eventClass, mh);
		} catch (Throwable e1) {
			// Method not found
			e1.printStackTrace();
		}
	}
	
	public Event activateState(Event e, StateContext context) {
		if (e != null) {
			try {
				MethodHandle mh = onActivateHandles.get(e.getClass());
				return (Event) mh.invoke(e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public Event receive(Event e, StateContext context) {
		if (e != null) {
			try {
				MethodHandle mh = onReceiveHandles.get(e.getClass());
				return (Event) mh.invoke(e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public void deactivateState(Event e, StateContext context) {
		if (e != null) {
			try {
				MethodHandle mh = onActivateHandles.get(e.getClass());
				mh.invoke(e, context);
			}
			catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	public static Event onActivate(Event e, StateContext context) { return null; }
	public static Event onActivate(AbortEvent e, StateContext context) { return null; }
	public static Event onActivate(DoneEvent e, StateContext context) { return null; }
	
	public static void onDeactivate(Event e, StateContext context) {}
	public static void onDeactivate(AbortEvent e, StateContext context) { }
	public static void onDeactivate(DoneEvent e, StateContext context) { }
	
	public static Event onReceive(Event e, StateContext context) { return null; }
}