package stackedStateMachine;

public class LeafState extends State {
	
	static class LeafStateContext extends StateContext {
		public LeafStateContext(StateContext stateContext) {
			super(stateContext);
		} 
	}
	public LeafState() {
		addOnActivateHandler(AbortEvent.class);
		addOnActivateHandler(DoneEvent.class);
		addOnDeactivateHandler(AbortEvent.class);
		addOnDeactivateHandler(DoneEvent.class);
	}
	
	public static Event onActivate(AbortEvent e, StateContext context) { return e; } 
	public static Event onActivate(DoneEvent e, StateContext context) { return null; }

	public static void onDeactivate(AbortEvent e, StateContext context) {	}
	public static void onDeactivate(DoneEvent e, StateContext context) {	}
}
