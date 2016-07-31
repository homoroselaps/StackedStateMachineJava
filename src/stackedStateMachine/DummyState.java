package stackedStateMachine;

public class DummyState extends DebugState {
	private int counter;
	public DummyState(int counter) {
		this.counter = counter;
	}
	@Override
	public State onGameTick(StackedStateMachine sender) {
		super.onGameTick(sender);
		counter--;
		if (counter <= 0) {
			return new DoneState();
		}
		return null;
	}
}
