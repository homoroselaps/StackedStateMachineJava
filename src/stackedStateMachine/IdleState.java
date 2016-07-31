package stackedStateMachine;

public class IdleState extends DebugState {
	
	@Override
	public State visitCarryEvent(CarryEvent e) {
		super.visitCarryEvent(e);
		return StateFactory.buildCarryState(e.from, e.to);
	}
	
	@Override
	public State visitAbortEvent(AbortEvent e) {
		super.visitAbortEvent(e);
		return null;
	}
	
	@Override
	public State visitDoneEvent(DoneEvent e) {
		super.visitDoneEvent(e);
		return null;
	}
}
