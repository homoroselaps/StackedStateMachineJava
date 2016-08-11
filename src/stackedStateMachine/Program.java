package stackedStateMachine;

import java.util.Scanner;
import java.util.function.IntConsumer;

class Point
{
	public float x, y;
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
class DebugState extends LeafState
{
	private void printDebug(String funcName, Event e) {
		//System.out.println(this.getClass().toString() + "." + funcName + "(" +(e!=null ?e.getClass().toString() : "") + ")");
	}
	
	@Override
	public Event receive(Event e, Object context) {
		printDebug("onRecieveEvent", e);
		return super.receive(e, context);
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
		addOnRecieveHandler(TimerEvent.class);
	}
	
	public Event onReceive(TimerEvent e, Object context) {
		counter--;
		if (counter <= 0) return new DoneEvent();
		return null;
	}
}

class PathingState extends DummyState
{
	static class PathingEvent extends Event
	{
		public Point target;
		public PathingEvent(Point target) {
			this.target = target;
		}	
	}
	
	public PathingState() { super(3); }
}

class DropState extends DummyState
{
	static class DropEvent extends Event { }
	public DropState() { super(1); }
}

class PickState extends DummyState
{
	static class PickEvent extends Event{}
	public PickState() { super(1); }
}

class CarryState extends DebugState
{
	static class CarryEvent extends Event
	{
		public Point from;
		public Point to;
		public CarryEvent(Point from, Point to) {
			this.from = from;
			this.to = to;
		}
	}
	
	public CarryState() {
		addOnActivateHandler(CarryEvent.class);
		addOnActivateHandler(DoneEvent.class);
	}

	private int stepCounter;
	private Point from, to;
	private Event controlAction(Object context) {
		stepCounter++;
		switch (stepCounter) {
			case 1:
				return new PathingState.PathingEvent(from);
			case 2:
				return new PickState.PickEvent();
			case 3:
				return new PathingState.PathingEvent(to);
			case 4:
				return new DropState.DropEvent();
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
	
	public Event onActivate(DoneEvent e, Object context) {
		return controlAction(context);
	}
}
class IdleState extends DebugState
{
	public IdleState() {
		addOnActivateHandler(AbortEvent.class);
	}
	
	public Event onActivate(AbortEvent e, Object context) {
		return null;
	}
}

class TimerEvent extends Event { }

public class Program {
	public static void main(String[] args) {
		System.out.println("Welome to the StackedStateMachine Testing Environment");
		System.out.println("Write 'exit' to end the program");
		System.out.println("Write 'carry' to send a CarryEvent");
		System.out.println("Write 'abort' to send an AbortEvent");
		System.out.println("Press 'enter' to send a TimerEvent");
		System.out.println("Write 'bench' to run a benchmark");
		
		// setup state machine
		IdleState is = new IdleState();
		CarryState cs = new CarryState();
		DropState ds = new DropState();
		PathingState ps = new PathingState();
		PickState pis = new PickState();
		
		StackedStateMachine ssm = new StackedStateMachine(is, null);
		ssm.addTransition(IdleState.class, CarryState.CarryEvent.class, () -> { return cs; });
		ssm.addTransition(CarryState.class, DropState.DropEvent.class, () -> { return ds; });
		ssm.addTransition(CarryState.class, PathingState.PathingEvent.class, () -> { return ps; });
		ssm.addTransition(CarryState.class, PickState.PickEvent.class, () -> { return pis; });
		
		// run the state machine
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine().toLowerCase();
			if (input.equals("exit"))
				break;
			else if (input.equals("carry"))
				ssm.raiseEvent(new CarryState.CarryEvent(new Point(5, 5), new Point(8, 8)));
			else if (input.equals("abort"))
				ssm.raiseEvent(new AbortEvent());
			else if (input.isEmpty())
				ssm.raiseEvent(new TimerEvent());
			else if (input.equals("bench"))
				benchmark((int i) -> { runTest(ssm);});
		}
		scanner.close();
	}
	
	private static void runTest(StackedStateMachine ssm) {
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new CarryState.CarryEvent(new Point(5, 5), new Point(8, 8)));
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new TimerEvent());
	}
	
	private static void benchmark(IntConsumer con) {
		System.out.println("# Startup finished.");
		for (float i = 6; i >= 0; i--)
		{
		    int executions = (int) Math.pow(10, i);
		    long start = System.nanoTime();
		    for (int j = 0; j <= executions - 1; j++)
		    {
		        con.accept(0);
		    }
		    long stop = System.nanoTime();
		    long delta = stop - start;
		    System.out.println("Invoke;\t" + executions + ";\t" + (delta/(executions)) + "ns");
		}
		System.out.println("# Shutdown finished.");
	}
}
