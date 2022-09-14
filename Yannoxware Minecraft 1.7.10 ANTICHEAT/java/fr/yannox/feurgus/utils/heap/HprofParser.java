package fr.yannox.feurgus.utils.heap;

import com.google.common.base.Preconditions;
import fr.yannox.feurgus.utils.heap.HprofParserException;
import fr.yannox.feurgus.utils.heap.RecordHandler;
import fr.yannox.feurgus.utils.heap.datastructures.AllocSite;
import fr.yannox.feurgus.utils.heap.datastructures.CPUSample;
import fr.yannox.feurgus.utils.heap.datastructures.ClassInfo;
import fr.yannox.feurgus.utils.heap.datastructures.Constant;
import fr.yannox.feurgus.utils.heap.datastructures.Instance;
import fr.yannox.feurgus.utils.heap.datastructures.InstanceField;
import fr.yannox.feurgus.utils.heap.datastructures.Static;
import fr.yannox.feurgus.utils.heap.datastructures.Type;
import fr.yannox.feurgus.utils.heap.datastructures.Value;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HprofParser {
    private RecordHandler handler;
    private HashMap<Long, ClassInfo> classMap;

    public HprofParser(RecordHandler handler) {
        this.handler = handler;
        this.classMap = new HashMap();
    }

    public void parse(File file) throws IOException {
        boolean done;
        FileInputStream fs = new FileInputStream(file);
        DataInputStream in = new DataInputStream(new BufferedInputStream(fs));
        String format = HprofParser.readUntilNull(in);
        int idSize = in.readInt();
        long startTime = in.readLong();
        this.handler.header(format, idSize, startTime);
        while (!(done = this.parseRecord(in, idSize, true))) {
        }
        in.close();
        FileInputStream fsSecond = new FileInputStream(file);
        DataInputStream inSecond = new DataInputStream(new BufferedInputStream(fsSecond));
        HprofParser.readUntilNull(inSecond);
        inSecond.readInt();
        inSecond.readLong();
        while (!(done = this.parseRecord(inSecond, idSize, false))) {
        }
        inSecond.close();
        this.handler.finished();
    }

    public static String readUntilNull(DataInput in) throws IOException {
        int bytesRead = 0;
        byte[] bytes = new byte[25];
        while ((bytes[bytesRead] = in.readByte()) != 0) {
            if (++bytesRead < bytes.length) continue;
            byte[] newBytes = new byte[bytesRead + 20];
            for (int i = 0; i < bytes.length; ++i) {
                newBytes[i] = bytes[i];
            }
            bytes = newBytes;
        }
        return new String(bytes, 0, bytesRead);
    }

    private boolean parseRecord(DataInput in, int idSize, boolean isFirstPass) throws IOException {
        byte tag;
        try {
            tag = in.readByte();
        }
        catch (EOFException e) {
            return true;
        }
        int time = in.readInt();
        long bytesLeft = Integer.toUnsignedLong(in.readInt());
        switch (tag) {
            case 1: {
                long l1 = HprofParser.readId(idSize, in);
                byte[] bArr1 = new byte[(int)(bytesLeft -= (long)idSize)];
                in.readFully(bArr1);
                if (!isFirstPass) break;
                this.handler.stringInUTF8(l1, new String(bArr1));
                break;
            }
            case 2: {
                int i1 = in.readInt();
                long l1 = HprofParser.readId(idSize, in);
                int i2 = in.readInt();
                long l2 = HprofParser.readId(idSize, in);
                if (!isFirstPass) break;
                this.handler.loadClass(i1, l1, i2, l2);
                break;
            }
            case 3: {
                int i1 = in.readInt();
                if (!isFirstPass) break;
                this.handler.unloadClass(i1);
                break;
            }
            case 4: {
                long l1 = HprofParser.readId(idSize, in);
                long l2 = HprofParser.readId(idSize, in);
                long l3 = HprofParser.readId(idSize, in);
                long l4 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                if (!isFirstPass) break;
                this.handler.stackFrame(l1, l2, l3, l4, i1, i2);
                break;
            }
            case 5: {
                int i1 = in.readInt();
                int i2 = in.readInt();
                int i3 = in.readInt();
                long[] lArr1 = new long[(int)(bytesLeft -= 12L) / idSize];
                for (int i = 0; i < lArr1.length; ++i) {
                    lArr1[i] = HprofParser.readId(idSize, in);
                }
                if (!isFirstPass) break;
                this.handler.stackTrace(i1, i2, i3, lArr1);
                break;
            }
            case 6: {
                short s1 = in.readShort();
                float f1 = in.readFloat();
                int i1 = in.readInt();
                int i2 = in.readInt();
                long l1 = in.readLong();
                long l2 = in.readLong();
                int i3 = in.readInt();
                AllocSite[] allocSites = new AllocSite[i3];
                for (int i = 0; i < allocSites.length; ++i) {
                    byte b1 = in.readByte();
                    int i4 = in.readInt();
                    int i5 = in.readInt();
                    int i6 = in.readInt();
                    int i7 = in.readInt();
                    int i8 = in.readInt();
                    int i9 = in.readInt();
                    allocSites[i] = new AllocSite(b1, i4, i5, i6, i7, i8, i9);
                }
                if (!isFirstPass) break;
                this.handler.allocSites(s1, f1, i1, i2, l1, l2, allocSites);
                break;
            }
            case 7: {
                int i1 = in.readInt();
                int i2 = in.readInt();
                long l1 = in.readLong();
                long l2 = in.readLong();
                if (isFirstPass) break;
                this.handler.heapSummary(i1, i2, l1, l2);
                break;
            }
            case 10: {
                int i1 = in.readInt();
                long l1 = HprofParser.readId(idSize, in);
                int i2 = in.readInt();
                long l2 = HprofParser.readId(idSize, in);
                long l3 = HprofParser.readId(idSize, in);
                long l4 = HprofParser.readId(idSize, in);
                if (!isFirstPass) break;
                this.handler.startThread(i1, l1, i2, l2, l3, l4);
                break;
            }
            case 11: {
                int i1 = in.readInt();
                if (!isFirstPass) break;
                this.handler.endThread(i1);
                break;
            }
            case 12: {
                if (isFirstPass) {
                    this.handler.heapDump();
                }
                while (bytesLeft > 0L) {
                    bytesLeft -= (long)this.parseHeapDump(in, idSize, isFirstPass);
                }
                if (isFirstPass) break;
                this.handler.heapDumpEnd();
                break;
            }
            case 28: {
                if (isFirstPass) {
                    this.handler.heapDumpSegment();
                }
                while (bytesLeft > 0L) {
                    bytesLeft -= (long)this.parseHeapDump(in, idSize, isFirstPass);
                }
                break;
            }
            case 44: {
                if (isFirstPass) break;
                this.handler.heapDumpEnd();
                break;
            }
            case 13: {
                int i1 = in.readInt();
                int i2 = in.readInt();
                CPUSample[] samples = new CPUSample[i2];
                for (int i = 0; i < samples.length; ++i) {
                    int i3 = in.readInt();
                    int i4 = in.readInt();
                    samples[i] = new CPUSample(i3, i4);
                }
                if (!isFirstPass) break;
                this.handler.cpuSamples(i1, samples);
                break;
            }
            case 14: {
                int i1 = in.readInt();
                short s1 = in.readShort();
                if (!isFirstPass) break;
                this.handler.controlSettings(i1, s1);
                break;
            }
            default: {
                throw new HprofParserException("Unexpected top-level record type: " + tag);
            }
        }
        return false;
    }

    private int parseHeapDump(DataInput in, int idSize, boolean isFirstPass) throws IOException {
        byte tag = in.readByte();
        int bytesRead = 1;
        switch (tag) {
            case -1: {
                long l1 = HprofParser.readId(idSize, in);
                if (isFirstPass) {
                    this.handler.rootUnknown(l1);
                }
                bytesRead += idSize;
                break;
            }
            case 1: {
                long l1 = HprofParser.readId(idSize, in);
                long l2 = HprofParser.readId(idSize, in);
                if (isFirstPass) {
                    this.handler.rootJNIGlobal(l1, l2);
                }
                bytesRead += 2 * idSize;
                break;
            }
            case 2: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                if (isFirstPass) {
                    this.handler.rootJNILocal(l1, i1, i2);
                }
                bytesRead += idSize + 8;
                break;
            }
            case 3: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                if (isFirstPass) {
                    this.handler.rootJavaFrame(l1, i1, i2);
                }
                bytesRead += idSize + 8;
                break;
            }
            case 4: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                if (isFirstPass) {
                    this.handler.rootNativeStack(l1, i1);
                }
                bytesRead += idSize + 4;
                break;
            }
            case 5: {
                long l1 = HprofParser.readId(idSize, in);
                if (isFirstPass) {
                    this.handler.rootStickyClass(l1);
                }
                bytesRead += idSize;
                break;
            }
            case 6: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                if (isFirstPass) {
                    this.handler.rootThreadBlock(l1, i1);
                }
                bytesRead += idSize + 4;
                break;
            }
            case 7: {
                long l1 = HprofParser.readId(idSize, in);
                if (isFirstPass) {
                    this.handler.rootMonitorUsed(l1);
                }
                bytesRead += idSize;
                break;
            }
            case 8: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                if (isFirstPass) {
                    this.handler.rootThreadObj(l1, i1, i2);
                }
                bytesRead += idSize + 8;
                break;
            }
            case 32: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                long l2 = HprofParser.readId(idSize, in);
                long l3 = HprofParser.readId(idSize, in);
                long l4 = HprofParser.readId(idSize, in);
                long l5 = HprofParser.readId(idSize, in);
                long l6 = HprofParser.readId(idSize, in);
                long l7 = HprofParser.readId(idSize, in);
                int i2 = in.readInt();
                bytesRead += idSize * 7 + 8;
                int s1 = in.readShort();
                bytesRead += 2;
                Preconditions.checkState(s1 >= 0);
                Constant[] constants = new Constant[s1];
                for (int i = 0; i < s1; ++i) {
                    short constantPoolIndex = in.readShort();
                    byte btype = in.readByte();
                    bytesRead += 3;
                    Type type = Type.hprofTypeToEnum(btype);
                    Value<Comparable<Boolean>> v = null;
                    switch (type) {
                        case OBJ: {
                            long vid = HprofParser.readId(idSize, in);
                            bytesRead += idSize;
                            v = new Value<Long>(type, vid);
                            break;
                        }
                        case BOOL: {
                            boolean vbool = in.readBoolean();
                            ++bytesRead;
                            v = new Value<Boolean>(type, vbool);
                            break;
                        }
                        case CHAR: {
                            char vc = in.readChar();
                            bytesRead += 2;
                            v = new Value<Character>(type, Character.valueOf(vc));
                            break;
                        }
                        case FLOAT: {
                            float vf = in.readFloat();
                            bytesRead += 4;
                            v = new Value<Float>(type, Float.valueOf(vf));
                            break;
                        }
                        case DOUBLE: {
                            double vd = in.readDouble();
                            bytesRead += 8;
                            v = new Value<Double>(type, vd);
                            break;
                        }
                        case BYTE: {
                            byte vbyte = in.readByte();
                            ++bytesRead;
                            v = new Value<Byte>(type, vbyte);
                            break;
                        }
                        case SHORT: {
                            short vs = in.readShort();
                            bytesRead += 2;
                            v = new Value<Short>(type, vs);
                            break;
                        }
                        case INT: {
                            int vi = in.readInt();
                            bytesRead += 4;
                            v = new Value<Integer>(type, vi);
                            break;
                        }
                        case LONG: {
                            long vl = in.readLong();
                            bytesRead += 8;
                            v = new Value<Long>(type, vl);
                        }
                    }
                    constants[i] = new Constant(constantPoolIndex, v);
                }
                int s2 = in.readShort();
                bytesRead += 2;
                Preconditions.checkState(s2 >= 0);
                Static[] statics = new Static[s2];
                for (int i = 0; i < s2; ++i) {
                    long staticFieldNameStringId = HprofParser.readId(idSize, in);
                    byte btype = in.readByte();
                    bytesRead += idSize + 1;
                    Type type = Type.hprofTypeToEnum(btype);
                    Value<Comparable<Boolean>> v = null;
                    switch (type) {
                        case OBJ: {
                            long vid = HprofParser.readId(idSize, in);
                            bytesRead += idSize;
                            v = new Value<Long>(type, vid);
                            break;
                        }
                        case BOOL: {
                            boolean vbool = in.readBoolean();
                            ++bytesRead;
                            v = new Value<Boolean>(type, vbool);
                            break;
                        }
                        case CHAR: {
                            char vc = in.readChar();
                            bytesRead += 2;
                            v = new Value<Character>(type, Character.valueOf(vc));
                            break;
                        }
                        case FLOAT: {
                            float vf = in.readFloat();
                            bytesRead += 4;
                            v = new Value<Float>(type, Float.valueOf(vf));
                            break;
                        }
                        case DOUBLE: {
                            double vd = in.readDouble();
                            bytesRead += 8;
                            v = new Value<Double>(type, vd);
                            break;
                        }
                        case BYTE: {
                            byte vbyte = in.readByte();
                            ++bytesRead;
                            v = new Value<Byte>(type, vbyte);
                            break;
                        }
                        case SHORT: {
                            short vs = in.readShort();
                            bytesRead += 2;
                            v = new Value<Short>(type, vs);
                            break;
                        }
                        case INT: {
                            int vi = in.readInt();
                            bytesRead += 4;
                            v = new Value<Integer>(type, vi);
                            break;
                        }
                        case LONG: {
                            long vl = in.readLong();
                            bytesRead += 8;
                            v = new Value<Long>(type, vl);
                        }
                    }
                    statics[i] = new Static(staticFieldNameStringId, v);
                }
                int s3 = in.readShort();
                bytesRead += 2;
                Preconditions.checkState(s3 >= 0);
                InstanceField[] instanceFields = new InstanceField[s3];
                for (int i = 0; i < s3; ++i) {
                    long fieldNameStringId = HprofParser.readId(idSize, in);
                    byte btype = in.readByte();
                    bytesRead += idSize + 1;
                    Type type = Type.hprofTypeToEnum(btype);
                    instanceFields[i] = new InstanceField(fieldNameStringId, type);
                }
                if (isFirstPass) {
                    this.classMap.put(l1, new ClassInfo(l1, l2, i2, instanceFields));
                }
                if (!isFirstPass) break;
                this.handler.classDump(l1, i1, l2, l3, l4, l5, l6, l7, i2, constants, statics, instanceFields);
                break;
            }
            case 33: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                long l2 = HprofParser.readId(idSize, in);
                int i2 = in.readInt();
                Preconditions.checkState(i2 >= 0);
                byte[] bArr1 = new byte[i2];
                in.readFully(bArr1);
                if (!isFirstPass) {
                    this.processInstance(new Instance(l1, i1, l2, bArr1), idSize);
                }
                bytesRead += idSize * 2 + 8 + i2;
                break;
            }
            case 34: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                long l2 = HprofParser.readId(idSize, in);
                Preconditions.checkState(i2 >= 0);
                long[] lArr1 = new long[i2];
                for (int i = 0; i < i2; ++i) {
                    lArr1[i] = HprofParser.readId(idSize, in);
                }
                if (isFirstPass) {
                    this.handler.objArrayDump(l1, i1, l2, lArr1);
                }
                bytesRead += (2 + i2) * idSize + 8;
                break;
            }
            case 35: {
                long l1 = HprofParser.readId(idSize, in);
                int i1 = in.readInt();
                int i2 = in.readInt();
                byte b1 = in.readByte();
                bytesRead += idSize + 9;
                Preconditions.checkState(i2 >= 0);
                Value[] vs = new Value[i2];
                Type t = Type.hprofTypeToEnum(b1);
                block52: for (int i = 0; i < vs.length; ++i) {
                    switch (t) {
                        case OBJ: {
                            long vobj = HprofParser.readId(idSize, in);
                            vs[i] = new Value<Long>(t, vobj);
                            bytesRead += idSize;
                            continue block52;
                        }
                        case BOOL: {
                            boolean vbool = in.readBoolean();
                            vs[i] = new Value<Boolean>(t, vbool);
                            ++bytesRead;
                            continue block52;
                        }
                        case CHAR: {
                            char vc = in.readChar();
                            vs[i] = new Value<Character>(t, Character.valueOf(vc));
                            bytesRead += 2;
                            continue block52;
                        }
                        case FLOAT: {
                            float vf = in.readFloat();
                            vs[i] = new Value<Float>(t, Float.valueOf(vf));
                            bytesRead += 4;
                            continue block52;
                        }
                        case DOUBLE: {
                            double vd = in.readDouble();
                            vs[i] = new Value<Double>(t, vd);
                            bytesRead += 8;
                            continue block52;
                        }
                        case BYTE: {
                            byte vbyte = in.readByte();
                            vs[i] = new Value<Byte>(t, vbyte);
                            ++bytesRead;
                            continue block52;
                        }
                        case SHORT: {
                            short vshort = in.readShort();
                            vs[i] = new Value<Short>(t, vshort);
                            bytesRead += 2;
                            continue block52;
                        }
                        case INT: {
                            int vi = in.readInt();
                            vs[i] = new Value<Integer>(t, vi);
                            bytesRead += 4;
                            continue block52;
                        }
                        case LONG: {
                            long vlong = in.readLong();
                            vs[i] = new Value<Long>(t, vlong);
                            bytesRead += 8;
                        }
                    }
                }
                if (!isFirstPass) break;
                this.handler.primArrayDump(l1, i1, b1, vs);
                break;
            }
            default: {
                throw new HprofParserException("Unexpected heap dump sub-record type: " + tag);
            }
        }
        return bytesRead;
    }

    private void processInstance(Instance i, int idSize) throws IOException {
        ByteArrayInputStream bs = new ByteArrayInputStream(i.packedValues);
        DataInputStream input = new DataInputStream(bs);
        ArrayList<Value<Boolean>> values = new ArrayList<Value<Boolean>>();
        long nextClass = i.classObjId;
        while (nextClass != 0L) {
            ClassInfo ci = this.classMap.get(nextClass);
            nextClass = ci.superClassObjId;
            for (InstanceField field : ci.instanceFields) {
                Value<Comparable<Boolean>> v = null;
                switch (field.type) {
                    case OBJ: {
                        long vid = HprofParser.readId(idSize, input);
                        v = new Value<Long>(field.type, vid);
                        break;
                    }
                    case BOOL: {
                        boolean vbool = input.readBoolean();
                        v = new Value<Boolean>(field.type, vbool);
                        break;
                    }
                    case CHAR: {
                        char vc = input.readChar();
                        v = new Value<Character>(field.type, Character.valueOf(vc));
                        break;
                    }
                    case FLOAT: {
                        float vf = input.readFloat();
                        v = new Value<Float>(field.type, Float.valueOf(vf));
                        break;
                    }
                    case DOUBLE: {
                        double vd = input.readDouble();
                        v = new Value<Double>(field.type, vd);
                        break;
                    }
                    case BYTE: {
                        byte vbyte = input.readByte();
                        v = new Value<Byte>(field.type, vbyte);
                        break;
                    }
                    case SHORT: {
                        short vs = input.readShort();
                        v = new Value<Short>(field.type, vs);
                        break;
                    }
                    case INT: {
                        int vi = input.readInt();
                        v = new Value<Integer>(field.type, vi);
                        break;
                    }
                    case LONG: {
                        long vl = input.readLong();
                        v = new Value<Long>(field.type, vl);
                    }
                }
                values.add(v);
            }
        }
        Value[] valuesArr = new Value[values.size()];
        valuesArr = values.toArray(valuesArr);
        this.handler.instanceDump(i.objId, i.stackTraceSerialNum, i.classObjId, valuesArr);
    }

    private static long readId(int idSize, DataInput in) throws IOException {
        long id = -1L;
        if (idSize == 4) {
            id = in.readInt();
            id &= 0xFFFFFFFFFFFFFFFFL;
        } else if (idSize == 8) {
            id = in.readLong();
        } else {
            throw new IllegalArgumentException("Invalid identifier size " + idSize);
        }
        return id;
    }

    private int mySkipBytes(int n, DataInput in) throws IOException {
        int bytesRead;
        try {
            for (bytesRead = 0; bytesRead < n; ++bytesRead) {
                in.readByte();
            }
        }
        catch (EOFException eOFException) {
            // empty catch block
        }
        return bytesRead;
    }
}
