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

public class Program {
	public static void main(String[] args) {
		System.out.println("Welome to the StackedStateMachine Testing Environment");
		System.out.println("Write 'exit' to end the program");
		System.out.println("Write 'carry' to send a CarryEvent");
		System.out.println("Write 'abort' to send an AbortEvent");
		System.out.println("Press 'enter' to send a TimerEvent");
        System.out.println("Write 'bench' to run a benchmark");
		
		// setup state machine
		StackedStateMachine ssm = new StackedStateMachine(StateFactory.buildIdleState());
		
		// run the state machine
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String input = scanner.nextLine().toLowerCase();
			if (input.equals("exit"))
				break;
			else if (input.equals("carry"))
				ssm.raiseEvent(new CarryEvent(new Point(5, 5), new Point(8, 8), ssm));
			else if (input.equals("abort"))
				ssm.abort();
			else if (input.isEmpty())
				ssm.raiseEvent(new TimerEvent(ssm));
			else if (input.equals("bench"))
				benchmark((int i)->{ runTest(ssm);});
		}
		scanner.close();
	}
    
    private static void runTest(StackedStateMachine ssm) {
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new CarryEvent(new Point(5, 5), new Point(8, 8), ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
        ssm.raiseEvent(new TimerEvent(ssm));
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
