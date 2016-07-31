package stackedStateMachine;

public class CarryEvent extends Event {

	@Override
	public State Accept(IStateVisitor state) {
		return state.visitCarryEvent(this);
	}
	
	public Point from;
	public Point to;
	public CarryEvent(Point from, Point to, StackedStateMachine sender) {
		super(sender);
		this.from = from;
		this.to = to;
	}
}
