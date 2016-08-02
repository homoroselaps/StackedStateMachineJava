package stackedStateMachine;

/**
 * State to simulate walking to a target position
 * @author homoroselaps
 *
 */
public class PathingState extends DummyState {
	Point target; 
	public PathingState(Point target) {
		super(3);
		this.target = target;
	}

}
