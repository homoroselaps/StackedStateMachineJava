package stackedStateMachine;

public interface IState
{
	State onGameTick(StackedStateMachine sender);
}
