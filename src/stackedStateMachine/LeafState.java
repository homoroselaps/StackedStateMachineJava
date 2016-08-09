package stackedStateMachine;

public class LeafState extends State {

	@Override
	protected Event onActivate(AbortEvent e, Object context) { return e; } 

	@Override
	protected Event onActivate(DoneEvent e, Object context) { return null; }

	@Override
	protected void onDeactivate(AbortEvent e, Object context) {	}

	@Override
	protected void onDeactivate(DoneEvent e, Object context) {	}
}
