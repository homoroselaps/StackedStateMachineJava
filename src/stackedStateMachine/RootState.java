package stackedStateMachine;

public class RootState extends State {

	@Override
	public Event onActivate(AbortEvent e, Object context) { return null;	}

	@Override
	public Event onActivate(DoneEvent e, Object context) { return null; }

	@Override
	public void onDeactivate(AbortEvent e, Object context) {	}

	@Override
	public void onDeactivate(DoneEvent e, Object context) {	}
}
