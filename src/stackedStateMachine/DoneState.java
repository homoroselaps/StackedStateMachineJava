package stackedStateMachine;

public final class DoneState extends State {
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		e.Sender.raiseEvent(new DoneEvent(e.Sender));
		return null;
	}
}
