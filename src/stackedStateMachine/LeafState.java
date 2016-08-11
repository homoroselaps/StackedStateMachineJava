package stackedStateMachine;

public class LeafState extends State {

	@Override
	public Event onActivate(AbortEvent e, StateContext context) { return e; } 

	@Override
	public Event onActivate(DoneEvent e, StateContext context) { return null; }

	@Override
	public void onDeactivate(AbortEvent e, StateContext context) {	}

	@Override
	public void onDeactivate(DoneEvent e, StateContext context) {	}
}
