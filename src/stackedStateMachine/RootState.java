package stackedStateMachine;

public class RootState extends State {
	@Override
	public Event onActivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = true; return null; }
	@Override
	public void onDeactivate(AbortEvent e, Object context, Out<Boolean> handled) { handled.v = true; }
	@Override
	public Event onActivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = true; return null; }
	@Override
	public void onDeactivate(DoneEvent e, Object context, Out<Boolean> handled) { handled.v = true; }
}
