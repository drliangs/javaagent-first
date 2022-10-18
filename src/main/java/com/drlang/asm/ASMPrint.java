package com.drlang.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.Textifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

public class ASMPrint {
    public static void main(String[] args) throws IOException {
        String className = "com.drlang.asm.HelleWord";
       int passingOptions = ClassReader.SKIP_FRAMES|ClassReader.SKIP_DEBUG;
        boolean asmCode =false;
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out,true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        new ClassReader(className).accept(traceClassVisitor,passingOptions);
    }
}
