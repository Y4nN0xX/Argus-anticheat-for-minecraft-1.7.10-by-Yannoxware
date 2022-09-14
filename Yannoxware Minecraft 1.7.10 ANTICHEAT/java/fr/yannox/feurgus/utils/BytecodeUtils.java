package fr.yannox.feurgus.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

public class BytecodeUtils {
    public static String[] getMethod(String className, String methodName, String methodDescriptor) throws IOException {
        ClassReader classReader = new ClassReader(className);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, new SourceCodeTextifier(), printWriter);
        MethodSelectorVisitor methodSelectorVisitor = new MethodSelectorVisitor(traceClassVisitor, methodName, methodDescriptor);
        classReader.accept(methodSelectorVisitor, 2);
        return BytecodeUtils.toList(stringWriter.toString());
    }

    public static String[] getMethod(String className, String methodName) throws IOException {
        return BytecodeUtils.getMethod(className, methodName, null);
    }

    private static String[] toList(String str) {
        String[] operations = str.split("[\n]");
        for (int i = 0; i < operations.length; ++i) {
            operations[i] = operations[i].trim();
        }
        return operations;
    }

    private static class SourceCodeTextifier
    extends Printer {
        public SourceCodeTextifier() {
            this(262144);
        }

        protected SourceCodeTextifier(int api) {
            super(api);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        }

        @Override
        public void visitSource(String file, String debug) {
        }

        @Override
        public void visitOuterClass(String owner, String name, String desc) {
        }

        @Override
        public Textifier visitClassAnnotation(String desc, boolean visible) {
            return new Textifier();
        }

        @Override
        public void visitClassAttribute(Attribute attr) {
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {
        }

        @Override
        public Textifier visitField(int access, String name, String desc, String signature, Object value) {
            return new Textifier();
        }

        @Override
        public Textifier visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            Textifier t = new Textifier();
            this.text.add(t.getText());
            return t;
        }

        @Override
        public void visitClassEnd() {
        }

        @Override
        public void visit(String name, Object value) {
        }

        @Override
        public void visitEnum(String name, String desc, String value) {
        }

        @Override
        public Textifier visitAnnotation(String name, String desc) {
            return new Textifier();
        }

        @Override
        public Textifier visitArray(String name) {
            return new Textifier();
        }

        @Override
        public void visitAnnotationEnd() {
        }

        @Override
        public Textifier visitFieldAnnotation(String desc, boolean visible) {
            return new Textifier();
        }

        @Override
        public void visitFieldAttribute(Attribute attr) {
            this.visitAttribute(attr);
        }

        @Override
        public void visitFieldEnd() {
        }

        @Override
        public Textifier visitAnnotationDefault() {
            return new Textifier();
        }

        @Override
        public Textifier visitMethodAnnotation(String desc, boolean visible) {
            return new Textifier();
        }

        @Override
        public Textifier visitParameterAnnotation(int parameter, String desc, boolean visible) {
            return new Textifier();
        }

        @Override
        public void visitMethodAttribute(Attribute attr) {
        }

        @Override
        public void visitCode() {
        }

        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        }

        @Override
        public void visitInsn(int opcode) {
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        }

        @Override
        public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object ... bsmArgs) {
        }

        @Override
        public void visitJumpInsn(int opcode, Label label) {
        }

        @Override
        public void visitLabel(Label label) {
        }

        @Override
        public void visitLdcInsn(Object cst) {
        }

        @Override
        public void visitIincInsn(int var, int increment) {
        }

        @Override
        public void visitTableSwitchInsn(int min, int max, Label dflt, Label ... labels) {
        }

        @Override
        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        }

        @Override
        public void visitMultiANewArrayInsn(String desc, int dims) {
        }

        @Override
        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        }

        @Override
        public void visitLineNumber(int line, Label start) {
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
        }

        @Override
        public void visitMethodEnd() {
        }

        public void visitAttribute(Attribute attr) {
        }

        @Override
        public Printer visitParameterAnnotation(int n, String string, boolean bl) {
            return this.visitParameterAnnotation(n, string, bl);
        }

        @Override
        public Printer visitMethodAnnotation(String string, boolean bl) {
            return this.visitMethodAnnotation(string, bl);
        }

        @Override
        public Printer visitAnnotationDefault() {
            return this.visitAnnotationDefault();
        }

        @Override
        public Printer visitFieldAnnotation(String string, boolean bl) {
            return this.visitFieldAnnotation(string, bl);
        }

        @Override
        public Printer visitArray(String string) {
            return this.visitArray(string);
        }

        @Override
        public Printer visitAnnotation(String string, String string2) {
            return this.visitAnnotation(string, string2);
        }

        @Override
        public Printer visitMethod(int n, String string, String string2, String string3, String[] stringArray) {
            return this.visitMethod(n, string, string2, string3, stringArray);
        }

        @Override
        public Printer visitField(int n, String string, String string2, String string3, Object object) {
            return this.visitField(n, string, string2, string3, object);
        }

        @Override
        public Printer visitClassAnnotation(String string, boolean bl) {
            return this.visitClassAnnotation(string, bl);
        }
    }

    private static class MaxVisitFilterMethodVisitor
    extends MethodVisitor {
        public MaxVisitFilterMethodVisitor(MethodVisitor mv) {
            super(262144, mv);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
        }
    }

    private static class MethodSelectorVisitor
    extends ClassVisitor {
        private final String methodName;
        private final String methodDescriptor;

        public MethodSelectorVisitor(ClassVisitor cv, String methodName, String methodDescriptor) {
            super(262144, cv);
            this.methodName = methodName;
            this.methodDescriptor = methodDescriptor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (this.methodName.equals(name)) {
                if (this.methodDescriptor == null) {
                    return new MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
                }
                if (this.methodDescriptor.equals(desc)) {
                    return new MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
                }
            }
            return null;
        }
    }
}
