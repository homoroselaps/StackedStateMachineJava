package stackedStateMachine;

public class RootState extends State {

	static class RootStateContext extends StateContext {
		public RootStateContext(StateContext stateContext) {
			super(stateContext);
		} 
	}
	public RootState() {
		addOnActivateHandler(AbortEvent.class);
		addOnActivateHandler(DoneEvent.class);
		addOnDeactivateHandler(AbortEvent.class);
		addOnDeactivateHandler(DoneEvent.class);
	}
	
	public static Event onActivate(AbortEvent e, StateContext context) { return null; }
	public static Event onActivate(DoneEvent e, StateContext context) { return null; }
	
	public static void onDeactivate(AbortEvent e, StateContext context) { }
	public static void onDeactivate(DoneEvent e, StateContext context) { }
}
