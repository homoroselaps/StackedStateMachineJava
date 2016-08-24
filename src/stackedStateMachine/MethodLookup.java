package stackedStateMachine;

import java.lang.invoke.MethodHandle;
import java.util.Arrays;
import java.util.IdentityHashMap;

@SuppressWarnings("rawtypes")
public class MethodLookup {
	
	IdentityHashMap<Class, MethodHandle> handles =  new IdentityHashMap<Class, MethodHandle>();
	
	public void add(Class cls, MethodHandle method) {
		handles.put(cls, method);
	}
	
	public MethodHandle get(Class cls) {
		//System.out.println(Arrays.toString(handles.keySet().toArray()));
		MethodHandle mh;
		if ((mh = handles.get(cls)) != null)
			return mh;
		//Iterate over all super classes
		Class it = cls;
		do { 
			it = it.getSuperclass(); 
		} while ((mh = handles.get(it)) == null && it != null);
		if (it != null) {
			// register found handle for the next lookup 
			handles.put(cls, mh);
			return mh;
		}
		return null;
	}
}