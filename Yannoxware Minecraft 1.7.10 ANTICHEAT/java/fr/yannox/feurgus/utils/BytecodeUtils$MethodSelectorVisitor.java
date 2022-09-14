package fr.yannox.feurgus.utils;

import fr.yannox.feurgus.utils.BytecodeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class BytecodeUtils$MethodSelectorVisitor
extends ClassVisitor {
    private final String methodName;
    private final String methodDescriptor;

    public BytecodeUtils$MethodSelectorVisitor(ClassVisitor cv, String methodName, String methodDescriptor) {
        super(262144, cv);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (this.methodName.equals(name)) {
            if (this.methodDescriptor == null) {
                return new BytecodeUtils.MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
            }
            if (this.methodDescriptor.equals(desc)) {
                return new BytecodeUtils.MaxVisitFilterMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
            }
        }
        return null;
    }
}
