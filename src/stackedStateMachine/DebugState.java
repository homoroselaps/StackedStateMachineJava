package stackedStateMachine;

/**
 * State for printing verbose Debug output
 * @author homoroselaps
 *
 */
public class DebugState extends State {
	private void printDebug(Event e) {
		//System.out.println(this.getClass().toString() + ": " + e.getClass().toString());
	}
	
	@Override
	public State visitNewEvent(NewEvent e) {
		printDebug(e);
		return super.visitNewEvent(e);
	}
	
	@Override
	public State visitDoneEvent(DoneEvent e) {
		printDebug(e);
		return super.visitDoneEvent(e);
	}
	
	@Override
	public State visitAbortEvent(AbortEvent e) {
		printDebug(e);
		return super.visitAbortEvent(e);
	}
	
	@Override
	public State visitTimerEvent(TimerEvent e) {
		printDebug(e);
		return super.visitTimerEvent(e);
	}
	
	
	@Override
	public State visitCarryEvent(CarryEvent e) {
		printDebug(e);
		return super.visitCarryEvent(e);
	}
}
