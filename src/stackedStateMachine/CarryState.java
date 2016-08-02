package stackedStateMachine;

/**
 * State to Control the single steps for carrying a resource 
 * @author homoroselaps
 * 1. Go to first position
 * 2. Pick up resource
 * 3. Go to second position
 * 4. Drop resource
 */
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
	
	/**
	 * On first activation initiate trigger first step 
	 */
	@Override
	public State visitNewEvent(NewEvent e) {
		super.visitNewEvent(e);
		return controlAction(e.context);
	}
	
	/**
	 * Whenever a sub state finished, trigger next step
	 */
	@Override
	public State visitDoneEvent(DoneEvent e) {
		super.visitDoneEvent(e);
		return controlAction(e.context);
	}
}
