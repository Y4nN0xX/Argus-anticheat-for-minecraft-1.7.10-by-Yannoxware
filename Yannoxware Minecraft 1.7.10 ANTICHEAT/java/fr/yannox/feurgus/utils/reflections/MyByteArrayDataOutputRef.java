package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.GetClassFromName;
import fr.yannox.feurgus.utils.reflections.GetMethodData2;
import fr.yannox.feurgus.utils.reflections.InvokeMethod2;
import java.lang.reflect.Method;

public class MyByteArrayDataOutputRef {
    private Object o;

    public MyByteArrayDataOutputRef() {
        this.generateDataOutput();
    }

    public void generateDataOutput() {
        try {
            Object classBao = GetClassFromName.fromName("java.io.ByteArrayOutputStream");
            Object classBytestreams = GetClassFromName.fromName("com.google.common.io.ByteStreams");
            Object inst = ((Class)classBao).newInstance();
            Object methodDt = GetMethodData2.getMethodFromClass(classBytestreams, "newDataOutput", new Class[]{(Class)classBao});
            this.o = InvokeMethod2.invk(methodDt, null, inst);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void writeByte(Object b) {
        try {
            Method m = this.o.getClass().getDeclaredMethod("write", Integer.TYPE);
            m.setAccessible(true);
            m.invoke(this.o, b);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void write(byte[] b) {
        try {
            Method m = this.o.getClass().getDeclaredMethod("write", byte[].class);
            m.setAccessible(true);
            m.invoke(this.o, new Object[]{b});
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void write(int b) {
        this.writeByte(b);
    }

    public void writeInt(Object b) {
        try {
            Method m = this.o.getClass().getDeclaredMethod("writeInt", Integer.TYPE);
            m.setAccessible(true);
            m.invoke(this.o, b);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void writeUTF(Object b) {
        try {
            Method m = this.o.getClass().getDeclaredMethod("writeUTF", String.class);
            m.setAccessible(true);
            m.invoke(this.o, b);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void writeLong(Object b) {
        try {
            Method m = this.o.getClass().getDeclaredMethod("writeLong", Long.TYPE);
            m.setAccessible(true);
            m.invoke(this.o, b);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public byte[] toByteArray() {
        try {
            Method m = this.o.getClass().getDeclaredMethod("toByteArray", new Class[0]);
            m.setAccessible(true);
            return (byte[])m.invoke(this.o, new Object[0]);
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }
}
