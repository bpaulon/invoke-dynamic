package bcp.invokedynamic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvokeDynamicTest {

	public static class SimpleClassLoader extends ClassLoader {
		public Class<?> defineClass(final String name, final byte[] b) {
			return defineClass(name, b, 0, b.length);
		}
	}

	private static Constructor<?> createClass(String className, Class<?> targetClass)
			throws Exception {

		byte[] bytecode = DynamicCallerBytecodeCreator.createBytes(className, "");
		writeToFile(bytecode, className);
		SimpleClassLoader loader = new SimpleClassLoader();
		Class<?> clazz = loader.defineClass(className, bytecode);

		return clazz.getConstructor(targetClass);
	}

	public static void main(final String[] args) throws Exception {
		TargetObject targetObject = new TargetObject();
		Constructor<?> constructor = createClass("bcp.invokedynamic.DynamicCaller",
				TargetObject.class);

		final Runnable caller = (Runnable) constructor.newInstance(targetObject);
		IntStream.range(1, 10).forEach(i -> caller.run());

	}

	public static void writeToFile(byte[] bytecode, String className) throws IOException {
		final String outputClassName = className.substring(className.lastIndexOf('.') + 1);
		File classFile = new File("target/classes/" + outputClassName + ".class");
		try (FileOutputStream fos = new FileOutputStream(classFile)) {
			fos.write(bytecode);
		}
		log.info("Class {} written to file {}", className, classFile);
	}

}
