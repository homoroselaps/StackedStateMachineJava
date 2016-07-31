package stackedStateMachine;

public class PathingState extends DummyState {
	Point target; 
	public PathingState(Point target) {
		super(3);
		this.target = target;
	}

}
