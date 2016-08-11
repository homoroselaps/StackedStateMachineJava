package stackedStateMachine;

import java.util.HashMap;

public class StateRegistry {
	private static HashMap<Class<? extends State>,State> map = new HashMap<Class<? extends State>,State>();
	protected StateRegistry() { }
	
	public static synchronized State getInstance(Class<? extends State> stateType) {
		State singleton = map.get(stateType);
		if(singleton != null) {
			return singleton;
		}
		try {
			singleton = stateType.newInstance();
		}
		catch(InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		map.put(stateType, singleton);
		return singleton;
	}
}