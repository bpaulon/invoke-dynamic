package bcp.invokedynamic;

import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static jdk.internal.org.objectweb.asm.Opcodes.ACC_SUPER;
import static jdk.internal.org.objectweb.asm.Opcodes.ALOAD;
import static jdk.internal.org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static jdk.internal.org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static jdk.internal.org.objectweb.asm.Opcodes.PUTFIELD;
import static jdk.internal.org.objectweb.asm.Opcodes.RETURN;
import static jdk.internal.org.objectweb.asm.Opcodes.V1_8;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.GeneratorAdapter;
import jdk.internal.org.objectweb.asm.commons.Method;

@SuppressWarnings("restriction")
public class DynamicCallerBytecodeCreator {

	private static final Handle BOOTSTRAP_METHOD =
	          new Handle(H_INVOKESTATIC, "bcp/invokedynamic/BootstrapMethods", "targetObjectClassBootstrap",
	                  MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class,
	                          MethodType.class).toMethodDescriptorString());
	
	
	/**
	 * Creates the bytecode of the class to be loaded in memory
	 * 
	 * @param outputClassName
	 * @param bsmName
	 * @param targetMethodDescriptor
	 * @return
	 * @throws Exception
	 */
	public static byte[] createBytes(String outputClassName, String bsmName)
			throws Exception {

		// the internal name of the target class 
		final String targetClass  = TargetObject.class.getCanonicalName().replace('.', '/');
		
		// reference to instance of target class
		final String targetReference = "L" + targetClass + ";";
		
		// the internal name of this class
		final String thisClass = outputClassName.replace('.', '/');
		
		// reference to instance of this class
		final String thisReference = "L" + thisClass + ";";
				
		
	    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
	    FieldVisitor fv;
	    MethodVisitor mv;

	    cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, thisClass, null, "java/lang/Object",
	            new String[]{"java/lang/Runnable"});

	    String compiledFromName = outputClassName.substring(outputClassName.lastIndexOf('.') + 1);
	    cw.visitSource(compiledFromName + ".java", null);

	    // create a field named "rt" of target type
	    {
	      fv = cw.visitField(ACC_PRIVATE, "rt", targetReference, null, null);
	      fv.visitEnd();
	    }
	    
	    {
	      mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + targetReference +")V", null, null);
	      mv.visitCode();
	      
	      Label l0 = new Label();
	      mv.visitLabel(l0);
	      mv.visitLineNumber(7, l0);
	      mv.visitVarInsn(ALOAD, 0);
	      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
	      
	      Label l1 = new Label();
	      mv.visitLabel(l1);
	      mv.visitLineNumber(8, l1);
	      mv.visitVarInsn(ALOAD, 0);
	      mv.visitVarInsn(ALOAD, 1);
	      mv.visitFieldInsn(PUTFIELD, thisClass, "rt", targetReference);
	      
	      Label l2 = new Label();
	      mv.visitLabel(l2);
	      mv.visitLineNumber(9, l2);
	      mv.visitInsn(RETURN);
	      
	      Label l3 = new Label();
	      mv.visitLabel(l3);
	      mv.visitLocalVariable("this", thisReference, null, l0, l3, 0);
	      mv.visitLocalVariable("rt", targetReference, null, l0, l3, 1);
	      
	      mv.visitMaxs(2, 2);
	      mv.visitEnd();
	    }
	    {
	      GeneratorAdapter ga = new GeneratorAdapter(ACC_PUBLIC, Method.getMethod("void run ()"), null, null, cw);
	      ga.loadThis();
	      ga.getField(Type.getType(thisClass), "rt", Type.getType(TargetObject.class));
	      
	     /*
	      * The signature of the method to call in the target class. It is a method on with no parameters 
	      * that returns void "()V"
	      */
	      ga.invokeDynamic("dynamicallyLinkedMethod", "(" + targetReference + ")V", BOOTSTRAP_METHOD);

//	      ga.loadThis();
//	      ga.getField(Type.getType("ocp/lambda/invokedynamic/DynamicCaller"), "rt", Type.getType(InvokeDynamicCreator.class));
//	      ga.invokeDynamic("bar", "(Locp/lambda/invokedynamic/InvokeDynamicCreator;)V", BOOTSTRAP_METHOD);
	      
	      
	      ga.returnValue();
	      ga.endMethod();
	    }
	    cw.visitEnd();

	    byte[] bytecode = cw.toByteArray();
	    return bytecode;
	}
}
