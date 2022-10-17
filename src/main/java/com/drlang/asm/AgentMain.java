package com.drlang.asm;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentMain {
    public static void premain(String arg, Instrumentation instrumentation) {

        System.out.println(arg);
        System.out.println("hello javaAgent");
        final String config = arg;
        final ClassPool classPool = new ClassPool();
        classPool.appendSystemPath();
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className == null || !className.replaceAll("/", ".").startsWith(config)) {
                    return null;

                }
                className = className.replaceAll("/", ".");
                CtClass ctClass = null;

                try {
                    ctClass = classPool.get(className);
                    for (CtMethod declaredClass : ctClass.getDeclaredMethods()) {
                        newMethod(declaredClass);
                    }
                    return ctClass.toBytecode();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static void newMethod(CtMethod declaredMethod) throws CannotCompileException, NotFoundException {
        CtMethod copy = CtNewMethod.copy(declaredMethod, declaredMethod.getDeclaringClass(), null);
        copy.setName(declaredMethod.getName() + "$agent");
        declaredMethod.getDeclaringClass().addMethod(copy);
        if (declaredMethod.getReturnType().equals(CtClass.voidType)) {
            declaredMethod.setBody(String.format(VOID_SOURCE,declaredMethod.getName()));
        }else {
            declaredMethod.setBody(String.format(SOURCE,declaredMethod.getName()));
        }
    }


    final static String SOURCE = "{   long begin = System.currentTimeMillis();\n" +
            "        Object result;\n" +
            "        try {\n" +
            "            result = ($w)%s$agent($$);\n" +
            "        } finally {\n" +
            "            long end = System.currentTimeMillis();\n" +
            "            System.out.println(end-begin);\n" +
            "        }\n" +
            "        return ($r)result;}";

    final static String VOID_SOURCE = "{        long begin = System.currentTimeMillis();\n" +
            "        try {\n" +
            "            %s$agent($$);\n" +
            "        } finally {\n" +
            "            long end = System.currentTimeMillis();\n" +
            "            System.out.println(end-begin);\n" +
            "        }\n}";
}
