package stackedStateMachine;

public class CarryState extends DebugState {
	private int stepCounter;
	private Point from, to;
	
	public CarryState(Point from, Point to) {
		this.from = from;
		this.to = to;
	}
	
	private State controlAction(StackedStateMachine sender) {
		stepCounter++;
		switch (stepCounter) {
			case 1:
				return StateFactory.buildPathingState(from);
			case 2:
				return StateFactory.buildPickState();
			case 3:
				return StateFactory.buildPathingState(to);
			case 4:
				return StateFactory.buildDropState();
			default:
				return new DoneState();
		}
	}
	
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		return controlAction(e.Sender);
	}
	
	@Override
	public State visitDoneEvent(DoneEvent e) {
		super.visitDoneEvent(e);
		return controlAction(e.Sender);
	}
}
