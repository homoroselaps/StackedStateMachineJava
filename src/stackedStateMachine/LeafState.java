package stackedStateMachine;

public class LeafState extends State {
	@Override
	public Event onActivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = true; return e; }
	@Override
	public void onDeactivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = true; }
	@Override
	public Event onActivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = true; return null; }
	@Override
	public void onDeactivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = true; }
}
