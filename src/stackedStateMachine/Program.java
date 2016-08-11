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

class InitialEvent extends Event { }

class DummyState extends LeafState
{
	static class DummyStateContext extends LeafStateContext {
		public int counter;
		
		public DummyStateContext(int counter, StateContext stateContext) {
			super(stateContext);
			this.counter = counter;
		}
	}
	
	public DummyState() {
		addOnRecieveHandler(TimerEvent.class);
	}
	
	public static Event onReceive(TimerEvent e, StateContext context) {
		DummyStateContext c = (DummyStateContext) context;
		c.counter--;
		if (c.counter <= 0) return new DoneEvent();
		return null;
	}
}

class PathingState extends DummyState
{
	static class PathingStateContext extends DummyStateContext {
		public PathingStateContext(StateContext stateContext) {
			super(3, stateContext);
		}	
	}
	
	@Override
	public StateContext buildContext(StateContext stateContext) {
		return new PathingStateContext(stateContext);
	}
	
	static class PathingEvent extends InitialEvent
	{
		public Point target;
		public PathingEvent(Point target) {
			this.target = target;
		}	
	}
}

class DropState extends DummyState
{
	static class DropStateContext extends DummyStateContext {
		public DropStateContext(StateContext stateContext) {
			super(1, stateContext);
		}	
	}
	@Override
	public StateContext buildContext(StateContext stateContext) {
		return new DropStateContext(stateContext);
	}
	
	static class DropEvent extends InitialEvent { }
}

class PickState extends DummyState
{
	static class PickStateContext extends DummyState.DummyStateContext {
		public PickStateContext(StateContext stateContext) {
			super(1, stateContext);
		}	
	}
	@Override
	public StateContext buildContext(StateContext stateContext) {
		return new PickStateContext(stateContext);
	}
	static class PickEvent extends InitialEvent { }
}

class CarryState extends LeafState
{
	static class CarryStateContext extends LeafStateContext {
		public int stepCounter;
		public Point from, to;
		public CarryStateContext(StateContext stateContext) {
			super(stateContext);
		}
	}
	
	@Override
	public StateContext buildContext(StateContext stateContext) {
		return new CarryStateContext(stateContext);
	}
	
	static class CarryEvent extends InitialEvent
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

	private static Event controlAction(StateContext context) {
		CarryStateContext c = (CarryStateContext) context;
		c.stepCounter++;
		switch (c.stepCounter) {
			case 1:
				return new PathingState.PathingEvent(c.from);
			case 2:
				return new PickState.PickEvent();
			case 3:
				return new PathingState.PathingEvent(c.to);
			case 4:
				return new DropState.DropEvent();
			default:
				return new DoneEvent();
		}
	}
	
	public static Event onActivate(CarryEvent e, StateContext context) {
		CarryStateContext c = (CarryStateContext) context;
		c.from = e.from;
		c.to = e.to;
		c.stepCounter = 0;
		return controlAction(context);            
	}
	
	public static Event onActivate(DoneEvent e, StateContext context) {
		return controlAction(context);
	}
}
class IdleState extends RootState
{
	
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
		
		StackedStateMachine ssm = new StackedStateMachine((RootState) StateRegistry.getInstance(IdleState.class), new StateContext(null));
		ssm.addTransition(IdleState.class, 	CarryState.CarryEvent.class, 		StateRegistry.getInstance(CarryState.class));
		ssm.addTransition(CarryState.class, DropState.DropEvent.class, 			StateRegistry.getInstance(DropState.class));
		ssm.addTransition(CarryState.class, PathingState.PathingEvent.class, 	StateRegistry.getInstance(PathingState.class));
		ssm.addTransition(CarryState.class, PickState.PickEvent.class, 			StateRegistry.getInstance(PickState.class));
		
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
	
	private static void runTestReceive(StackedStateMachine ssm) {
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
