package stackedStateMachine;

public abstract class DoneEvent extends Event { }

class SuccessEvent extends Event { }
class FailureEvent extends Event { }
