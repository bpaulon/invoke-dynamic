package bcp.invokedynamic;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BootstrapMethods {

	/**
	 * The method creates the CallSite to be invoked by the DynamicCaller class
	 * constructed
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

		final MethodHandle barMethodHandler = lookup.findVirtual(TargetObject.class, "bar",
				MethodType.methodType(void.class));

		final MethodHandle bazMethodHandler = lookup.findVirtual(TargetObject.class, "baz",
				MethodType.methodType(void.class));

		log.info("Created target Method Handler: {}", barMethodHandler);
		CallSite cs = new MutableCallSite(barMethodHandler);
		return cs;
	}
}
