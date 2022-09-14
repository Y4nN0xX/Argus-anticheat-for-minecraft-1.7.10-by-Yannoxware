package fr.yannox.feurgus.utils.heap;

import com.google.common.io.ByteArrayDataOutput;

public class StackMap {
    private final long frameId;
    private final String className;
    private final String method;
    private final String methodSig;
    private final String sourceFile;

    public StackMap(long frameId, String className, String method, String methodSig, String sourceFile) {
        this.frameId = frameId;
        this.className = className;
        this.method = method;
        this.methodSig = methodSig;
        this.sourceFile = sourceFile;
    }

    public ByteArrayDataOutput writeStream(ByteArrayDataOutput stream) {
        stream.writeLong(this.frameId);
        stream.writeUTF(this.className);
        stream.writeUTF(this.method);
        stream.writeUTF(this.methodSig);
        stream.writeUTF(this.sourceFile);
        return stream;
    }
}
