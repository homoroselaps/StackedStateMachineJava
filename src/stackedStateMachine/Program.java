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

public class Program {
	public static void main(String[] args) {
		System.out.println("Welome to the StackedStateMachine Testing Environment");
		System.out.println("Write 'exit' to end the program");
		System.out.println("Write 'carry' to send a CarryEvent");
		System.out.println("Write 'abort' to send an AbortEvent");
		System.out.println("Press 'enter' to step forward");
		
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
			else if (input.isEmpty()) {
				ssm.handleTransition(ssm.getState().onGameTick(ssm));
			}
		}
		scanner.close();
	}
}
