package stackedStateMachine;

/**
 * Factory to create instances of the states specific for a use case.
 * @author homoroselaps
 *
 */
public final class StateFactory {
	private StateFactory() { }
	static PathingState buildPathingState(Point target) {
		return new PathingState(target);
	}
	static CarryState buildCarryState(Point from, Point to) {
		return new CarryState(from, to);
	}
	static PickState buildPickState() {
		return new PickState();
	}
	static DropState buildDropState() {
		return new DropState();
	}
	public static State buildIdleState() {
		return new IdleState();
	}
}
