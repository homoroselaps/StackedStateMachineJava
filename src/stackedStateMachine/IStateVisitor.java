package stackedStateMachine;

public interface IStateVisitor {
	default State visitAbortEvent(AbortEvent e) {
		return new AbortState(); 
	}
	default State visitDoneEvent(DoneEvent e) { return null; }
	default State visitNewEvent(NewEvent e) { return null; }
	default State visitCarryEvent(CarryEvent e) { return null; }
}
