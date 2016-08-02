package stackedStateMachine;

/**
 * Carry event to trigger transport of a resource 
 * @author homoroselaps
 *
 */
public class CarryEvent extends Event {

	@Override
	public State Accept(State state) {
		return state.visitCarryEvent(this);
	}
	public Point from;
	public Point to;
	
	/**
	 * Constructor of CarryEvent
	 * @param from Position where to find the resource
	 * @param to Position where to drop the resource
	 * @param context Sender of the Event
	 */
	public CarryEvent(Point from, Point to, StackedStateMachine context) {
		super(context);
		this.from = from;
		this.to = to;
	}
}
