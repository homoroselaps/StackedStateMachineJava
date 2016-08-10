package stackedStateMachine;

abstract class BaseEvent {
	public Event activate(State state, Object context, Out<Boolean> handled) { return null; }
	public Event recieve(State state, Object context, Out<Boolean> handled) { return null; }
	public void deactivate(State state, Object context, Out<Boolean> handled) { }
}

abstract class Event extends BaseEvent {
	public interface fn {
		default Event onActivate(Event e, Object context, Out<Boolean> handled) {return null; }
		default Event onRecieve(Event e, Object context, Out<Boolean> handled) { return null; }
		default void onDeactivate(Event e, Object context, Out<Boolean> handled) { }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((Event)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((Event)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((Event)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}
