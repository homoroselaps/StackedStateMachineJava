package stackedStateMachine;

public final class AbortState extends State {
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		e.Sender.raiseEvent(new AbortEvent(e.Sender));
		return null;
	}
}
