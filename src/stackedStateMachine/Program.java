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
	public Event recieveEvent(Event e, Object context) {
		printDebug("onRecieveEvent", e);
		return super.recieveEvent(e, context);
	}
	@Override
	public Event activateState(Event e, Object context) {
		printDebug("onActivate", e);
		return super.activateState(e, context);
	}
	@Override
	public void deactivateState(Event e, Object context) {
		printDebug("onDeactivate", e);
		super.deactivateState(e, context);
	}
}
class DummyState extends DebugState
{
	private int counter;
	public DummyState(int counter) {
		this.counter = counter;
		this.addOnRecieveHandler(TimerEvent.class, (Event e, Object context) -> { return onRecieveEvent((TimerEvent)e, context); });
	}
	
	public Event onRecieveEvent(TimerEvent e, Object context) {
		counter--;
		if (counter <= 0) return new DoneEvent();
		return null;
	}
}

class PathingEvent extends Event
{
	public Point target;
	public PathingEvent(Point target) {
		this.target = target;
	}
	
}
class PathingState extends DummyState
{
	public PathingState() { super(3); }
}

class DropEvent extends Event {
}

class DropState extends DummyState
{
	public DropState() { super(1); }
}

class PickEvent extends Event {
}

class PickState extends DummyState
{
	public PickState() { super(1); }
}

class CarryEvent extends Event
{
	public Point from;
	public Point to;
	public CarryEvent(Point from, Point to) {
		this.from = from;
		this.to = to;
	}
}
class CarryState extends DebugState
{
	public CarryState() {
		addOnActivateHandler(CarryEvent.class, (Event e, Object context) -> { return onActivate((CarryEvent)e, context); });
	}

	private int stepCounter;
	private Point from, to;
	private Event controlAction(Object context) {
		stepCounter++;
		switch (stepCounter) {
			case 1:
				return new PathingEvent(from);
			case 2:
				return new PickEvent();
			case 3:
				return new PathingEvent(to);
			case 4:
				return new DropEvent();
			default:
				return new DoneEvent();
		}
	}
	public Event onActivate(CarryEvent e, Object context) {
		from = e.from;
		to = e.to;
		stepCounter = 0;
		return controlAction(context);            
	}
	@Override
	protected Event onActivate(DoneEvent e, Object context) {
		return controlAction(context);
	}
}
class IdleState extends DebugState
{
	public IdleState() { }
	@Override
	protected Event onActivate(AbortEvent e, Object context) {
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
		StackedStateMachine ssm = new StackedStateMachine(new IdleState(), null);
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
				ssm.raiseEvent(new CarryEvent(new Point(5, 5), new Point(8, 8)));
			else if (input.equals("abort"))
				ssm.raiseEvent(new AbortEvent());
			else if (input.isEmpty()) {
				ssm.raiseEvent(new TimerEvent());
			}
		}
		scanner.close();
	}
}
