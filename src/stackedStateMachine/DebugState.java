package stackedStateMachine;

public class DebugState extends State {
	private void printDebug(String funcName, Event e) {
		System.out.println(this.getClass().toString() + "." + funcName);
	}
	@Override
	public State onGameTick(StackedStateMachine sender) {
		printDebug("onGameTick", null);
		return super.onGameTick(sender);
	}
	
	@Override
	public State visitNewEvent(NewEvent e) {
		printDebug("NewEvent", e);
		return super.visitNewEvent(e);
	}
	
	@Override
	public State visitDoneEvent(DoneEvent e) {
		printDebug("DoneEvent", e);
		return super.visitDoneEvent(e);
	}
	
	@Override
	public State visitAbortEvent(AbortEvent e) {
		printDebug("AbortEvent", e);
		return super.visitAbortEvent(e);
	}
	
	@Override
	public State visitCarryEvent(CarryEvent e) {
		printDebug("CarryEvent", e);
		return super.visitCarryEvent(e);
	}
}
