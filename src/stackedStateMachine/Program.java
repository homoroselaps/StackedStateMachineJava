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

class DummyState extends LeafState
{
	private int counter;
	public DummyState(int counter) {
		this.counter = counter;
	}
	
	@Override
	public Event onRecieve(TimerEvent e, Object context, Out<Boolean> handled) {
		printDebug("onRecieve", e);
		handled.v = true;
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
	public interface fn {
		default Event onActivate(PathingEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(PathingEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(PathingEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((PathingEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((PathingEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((PathingEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}

class PathingState extends DummyState
{	
	public PathingState() { super(3); }
}

class DropEvent extends Event {
	public interface fn {
		default Event onActivate(DropEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(DropEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(DropEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((DropEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((DropEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((DropEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}

class DropState extends DummyState
{
	public DropState() { super(1); }
}

class PickEvent extends Event{
	public interface fn {
		default Event onActivate(PickEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(PickEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(PickEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((PickEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((PickEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((PickEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
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
	public CarryEvent(Point from, Point to) {
		this.from = from;
		this.to = to;
	}
	public interface fn {
		default Event onActivate(CarryEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(CarryEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(CarryEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((CarryEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((CarryEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((CarryEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}

class CarryState extends LeafState
{
	public CarryState() { }

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
	@Override
	public Event onActivate(CarryEvent e, Object context, Out<Boolean> handled) {
		printDebug("onActivate", e);
		handled.v = true;
		from = e.from;
		to = e.to;
		stepCounter = 0;
		return controlAction(context);            
	}
	@Override
	public Event onActivate(DoneEvent e, Object context, Out<Boolean> handled) {
		printDebug("onActivate", e);
		handled.v = true;
		return controlAction(context);
	}
}
class IdleState extends RootState
{
	public IdleState() { }
	
	@Override
	public Event onActivate(AbortEvent e, Object context, Out<Boolean> handled) {
		printDebug("onActivate", e);
		handled.v = true;
		return null;
	}
	
	@Override
	public Event onRecieve(TimerEvent e, Object context, Out<Boolean> handled) {
		printDebug("onRecieve", e);
		handled.v = true;
		return null;
	}	
}

class TimerEvent extends Event {
	public interface fn {
		default Event onActivate(TimerEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default Event onRecieve(TimerEvent e, Object context, Out<Boolean> handled) { handled.v = false; return null; }
		default void onDeactivate(TimerEvent e, Object context, Out<Boolean> handled) { handled.v = false; }
	}
	
	@Override
	public Event activate(State state, Object context, Out<Boolean> handled) {
		Event event = state.onActivate((TimerEvent)this, context, handled);
		return handled.v ? event : super.activate(state, context, handled);
	}
	
	@Override
	public Event recieve(State state, Object context, Out<Boolean> handled) {
		Event event = state.onRecieve((TimerEvent)this, context, handled);
		return handled.v ? event : super.recieve(state, context, handled);
	}
	
	@Override
	public void deactivate(State state, Object context, Out<Boolean> handled) {
		state.onDeactivate((TimerEvent)this, context, handled);
		if (!handled.v)
			super.deactivate(state, context, handled);
	}
}

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
		ssm.addTransition(IdleState.class, CarryEvent.class, () -> { return cs; });
		ssm.addTransition(CarryState.class, DropEvent.class, () -> { return ds; });
		ssm.addTransition(CarryState.class, PathingEvent.class, () -> { return ps; });
		ssm.addTransition(CarryState.class, PickEvent.class, () -> { return pis; });
		
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
			else if (input.isEmpty())
				ssm.raiseEvent(new TimerEvent());
			else if (input.equals("bench"))
				benchmark((int i) -> { runTest(ssm);});
		
		}
		scanner.close();
	}
	
	private static void runTest(StackedStateMachine ssm) {
		ssm.raiseEvent(new TimerEvent());
		ssm.raiseEvent(new CarryEvent(new Point(5, 5), new Point(8, 8)));
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
