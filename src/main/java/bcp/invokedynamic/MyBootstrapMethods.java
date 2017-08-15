package bcp.invokedynamic;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyBootstrapMethods {

	static String methodName;
	/**
	 * The method creates the CallSite to be invoked by the DynamicCaller. The bootstrap 
	 * method is called only once and the invokeDynamic instruction is linked to the 
	 * returned CallSite. 
	 * Each invokeDynamic instruction is linked to one CallSite even if the instructions 
	 * are identical. After the above linking the target method in the CallSite is called
	 * directly. 
	 * 
	 * We alternate the creation of CallSite objects to point to the bar() method 
	 * in TargetObject and then to the baz() method. The caller, for the same instruction set,
	 * will call either bar() or baz() depending on the linked CallSite
	 * 
	 * @param caller
	 * @param name
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public static CallSite targetObjectClassBootstrap(MethodHandles.Lookup caller, String name,
			MethodType type) throws Throwable {
		log.info("Bootstrap method called. Caller: {}, name: {}, type: {}", caller, name, type);

		final MethodHandles.Lookup lookup = MethodHandles.lookup();

		methodName = (methodName == "bar" ? "baz" : "bar");
		final MethodHandle barMethodHandler = lookup.findVirtual(TargetObject.class, methodName,
				MethodType.methodType(void.class));

		log.info("Created target Method Handler: {}", barMethodHandler);
		CallSite cs = new MutableCallSite(barMethodHandler);
		return cs;
	}
}
