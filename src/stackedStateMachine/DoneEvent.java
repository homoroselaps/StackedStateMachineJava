package stackedStateMachine;

public class DoneEvent extends Event {
	public interface fn {
		default Event onActivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((DoneEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((DoneEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((DoneEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}
