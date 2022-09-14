package fr.yannox.feurgus.utils;

import org.objectweb.asm.MethodVisitor;

class BytecodeUtils$MaxVisitFilterMethodVisitor
extends MethodVisitor {
    public BytecodeUtils$MaxVisitFilterMethodVisitor(MethodVisitor mv) {
        super(262144, mv);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
    }
}
