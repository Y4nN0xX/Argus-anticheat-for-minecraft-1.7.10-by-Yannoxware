package fr.yannox.feurgus.utils.reflections;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class MyByteArrayDataOutput {
    private final ByteArrayDataOutput stream = ByteStreams.newDataOutput();

    public void writeByte(Object b) {
        this.stream.writeByte(((Byte)b).byteValue());
    }

    public void write(byte[] b) {
        this.stream.write(b);
    }

    public void write(byte b) {
        this.stream.write(b);
    }

    public void write(int b) {
        this.stream.write(b);
    }

    public void writeInt(int b) {
        this.stream.writeInt(b);
    }

    public void writeLong(long b) {
        this.stream.writeLong(b);
    }

    public void writeUTF(String b) {
        this.stream.writeUTF(b);
    }

    public byte[] toByteArray() {
        return this.stream.toByteArray();
    }
}
