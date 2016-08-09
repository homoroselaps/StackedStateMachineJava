package stackedStateMachine;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.HashMap;

public class MethodLookup {
	HashMap<Class, MethodHandle> handles =  new HashMap<Class, MethodHandle>();
	public void add(Class cls, MethodHandle method) {
		handles.put(cls, method);
	}
	@SuppressWarnings("rawtypes")
	public MethodHandle get(Class cls) {
		//System.out.println(Arrays.toString(handles.keySet().toArray()));
		if (handles.containsKey(cls))
			return handles.get(cls);
		//Iterate over all super classes
		//System.out.println(cls);
		Class it = cls;
		//System.out.println(it);
		do { 
			it = it.getSuperclass(); 
		} while (!handles.containsKey(it) && it != null);
		if (it != null) {
			MethodHandle mh = handles.get(it);
			// register found handle for for the next lookup 
			handles.put(cls, mh);
			return mh;
		}
		return null;
	}
}
