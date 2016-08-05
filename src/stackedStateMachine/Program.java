package stackedStateMachine;

import java.util.Scanner;

class Point
{
	public float x, y;
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
class DebugState extends State
{
	private void printDebug(String funcName, Event e) {
		System.out.println(this.getClass().toString() + "." + funcName + "(" +(e!=null ?e.getClass().toString() : "") + ")");
	}
	@Override
	public Event recieveEvent(Event e) {
		printDebug("onRecieveEvent", e);
		return super.recieveEvent(e);
	}
	@Override
	public Event activateState(Event e) {
		printDebug("onActivate", e);
		return super.activateState(e);
	}
	@Override
	public void deactivateState(Event e) {
		printDebug("onDeactivate", e);
		super.deactivateState(e);
	}
}
class DummyState extends DebugState
{
	private int counter;
	public DummyState(int counter) {
		this.counter = counter;
		this.addOnRecieveHandler(TimerEvent.class, (Event e) -> { return onRecieveEvent((TimerEvent)e); });
	}
	
	public Event onRecieveEvent(TimerEvent e) {
		counter--;
		if (counter <= 0) return new DoneEvent(e.context);
		return null;
	}
}

class PathingEvent extends Event
{
	public Point target;
	public PathingEvent(Point target, Object context) {
		super(context);
		this.target = target;
	}
	
}
class PathingState extends DummyState
{
	public PathingState() { super(3); }
}

class DropEvent extends Event {

	public DropEvent(Object context) {
		super(context);
	} 
}

class DropState extends DummyState
{
	public DropState() { super(1); }
}

class PickEvent extends Event {

	public PickEvent(Object context) {
		super(context);
	} 
}

class PickState extends DummyState
{
	public PickState() { super(1); }
}

class CarryEvent extends Event
{
	public Point from;
	public Point to;
	public CarryEvent(Point from, Point to, Object context) {
		super(context);
		this.from = from;
		this.to = to;
	}
}
class CarryState extends DebugState
{
	public CarryState() {
		addOnActivateHandler(CarryEvent.class, (Event e) -> { return onActivate((CarryEvent)e); });
	}

	private int stepCounter;
	private Point from, to;
	private Event controlAction(Object context) {
		stepCounter++;
		switch (stepCounter) {
			case 1:
				return new PathingEvent(from, context);
			case 2:
				return new PickEvent(context);
			case 3:
				return new PathingEvent(to, context);
			case 4:
				return new DropEvent(context);
			default:
				return new DoneEvent(context);
		}
	}
	public Event onActivate(CarryEvent e) {
		from = e.from;
		to = e.to;
		stepCounter = 0;
		return controlAction(e.context);            
	}
	@Override
	protected Event onActivate(DoneEvent e) {
		return controlAction(e.context);
	}
}
class IdleState extends DebugState
{
	public IdleState() { }
	@Override
	protected Event onActivate(AbortEvent e) {
		return null;
	}
}

public class Program {
	public static void main(String[] args) {
		System.out.println("Welome to the StackedStateMachine Testing Environment");
		System.out.println("Write 'exit' to end the program");
		System.out.println("Write 'carry' to send a CarryEvent");
		System.out.println("Write 'abort' to send an AbortEvent");
		System.out.println("Press 'enter' to step forward");
		
		// setup state machine
		StackedStateMachine ssm = new StackedStateMachine(new IdleState());
		ssm.addTransition(IdleState.class, CarryEvent.class, () -> { return new CarryState(); });
		ssm.addTransition(CarryState.class, DropEvent.class, () -> { return new DropState(); });
		ssm.addTransition(CarryState.class, PathingEvent.class, () -> { return new PathingState(); });
		ssm.addTransition(CarryState.class, PickEvent.class, () -> { return new PickState(); });
		
		// run the state machine
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine().toLowerCase();
			if (input.equals("exit"))
				break;
			else if (input.equals("carry"))
				ssm.raiseEvent(new CarryEvent(new Point(5, 5), new Point(8, 8), null));
			else if (input.equals("abort"))
				ssm.raiseEvent(new AbortEvent(null));
			else if (input.isEmpty()) {
				ssm.raiseEvent(new TimerEvent(null));
			}
		}
		scanner.close();
	}
}
