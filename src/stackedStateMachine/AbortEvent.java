package stackedStateMachine;

public class AbortEvent extends Event {
	public interface fn {
		default Event onActivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((AbortEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((AbortEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((AbortEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}
