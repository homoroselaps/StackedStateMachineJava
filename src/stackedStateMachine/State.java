package stackedStateMachine;
import stackedStateMachine.Event;
import stackedStateMachine.AbortEvent;

public abstract class State implements 
Event.fn, 
AbortEvent.fn, 
DoneEvent.fn, 
TimerEvent.fn,
CarryEvent.fn,
PathingEvent.fn,
PickEvent.fn,
DropEvent.fn
{				
	
}
