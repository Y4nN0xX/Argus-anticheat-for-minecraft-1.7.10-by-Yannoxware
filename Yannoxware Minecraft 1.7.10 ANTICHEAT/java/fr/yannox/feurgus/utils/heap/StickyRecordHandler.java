package fr.yannox.feurgus.utils.heap;

import fr.yannox.feurgus.utils.heap.RecordHandler;
import fr.yannox.feurgus.utils.heap.StackMap;
import fr.yannox.feurgus.utils.heap.datastructures.AllocSite;
import fr.yannox.feurgus.utils.heap.datastructures.CPUSample;
import fr.yannox.feurgus.utils.heap.datastructures.Constant;
import fr.yannox.feurgus.utils.heap.datastructures.InstanceField;
import fr.yannox.feurgus.utils.heap.datastructures.Static;
import fr.yannox.feurgus.utils.heap.datastructures.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StickyRecordHandler
implements RecordHandler {
    private Map<Long, String> mapping = new HashMap<Long, String>();
    private Map<Integer, String> mappingThreadSerial = new HashMap<Integer, String>();
    private Map<Integer, String> mappingClass = new HashMap<Integer, String>();
    private List<StackMap> stackMap = new ArrayList<StackMap>();
    private Set<Object> classes = new HashSet<Object>();
    private Set<Object> threads = new HashSet<Object>();
    private Map<Long, Long> objectIds = new HashMap<Long, Long>();
    private List<Long> jniObjectIds = new ArrayList<Long>();

    @Override
    public void header(String format, int idSize, long time) {
    }

    @Override
    public void stringInUTF8(long id, String data) {
        this.mapping.put(id, data);
    }

    private void addClass(long classNameStringId) {
        if (this.mapping.containsKey(classNameStringId)) {
            String className = this.mapping.get(classNameStringId);
            if (className.equals("[S") || className.equals("[Z") || className.equals("[C") || className.equals("[I") || className.equals("[F") || className.equals("[J") || className.equals("[L") || className.equals("[B") || className.equals("[[S") || className.equals("[[Z") || className.equals("[[C") || className.equals("[[I") || className.equals("[[D") || className.equals("[[F") || className.equals("[[J") || className.equals("[[L") || className.equals("[[B") || className.equals("[D")) {
                return;
            }
            if (className.startsWith("[L")) {
                className = className.replace("[L", "");
            }
            if (className.startsWith("[[L")) {
                className = className.replace("[[L", "");
            }
            if (className.endsWith(";") && className.length() > 1) {
                className = className.substring(0, className.length() - 1);
            }
            if (className.contains("/")) {
                className = className.replace("/", ".");
            }
            if (className.startsWith("java.") || className.startsWith("jdk.")) {
                return;
            }
            if (className.contains("$")) {
                className = className.substring(0, className.lastIndexOf("$"));
            }
            this.classes.add(className);
        }
    }

    @Override
    public void loadClass(int classSerialNum, long classObjId, int stackTraceSerialNum, long classNameStringId) {
        this.addClass(classNameStringId);
        this.mappingClass.put(classSerialNum, this.mapping.get(classNameStringId));
    }

    @Override
    public void unloadClass(int classSerialNum) {
    }

    @Override
    public void stackFrame(long stackFrameId, long methodNameStringId, long methodSigStringId, long sourceFileNameStringId, int classSerialNum, int location) {
        String mappingClazz = this.mappingClass.get(classSerialNum);
        String methodName = this.mapping.get(methodNameStringId);
        String methodSigName = this.mapping.get(methodSigStringId);
        String sourceFile = this.mapping.get(sourceFileNameStringId);
        this.stackMap.add(new StackMap(stackFrameId, mappingClazz, methodName, methodSigName, sourceFile));
    }

    @Override
    public void stackTrace(int stackTraceSerialNum, int threadSerialNum, int numFrames, long[] stackFrameIds) {
    }

    @Override
    public void allocSites(short bitMaskFlags, float cutoffRatio, int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated, AllocSite[] sites) {
    }

    @Override
    public void heapSummary(int totalLiveBytes, int totalLiveInstances, long totalBytesAllocated, long totalInstancesAllocated) {
    }

    @Override
    public void startThread(int threadSerialNum, long threadObjectId, int stackTraceSerialNum, long threadNameStringId, long threadGroupNameId, long threadParentGroupNameId) {
        if (this.mapping.containsKey(threadNameStringId)) {
            this.mappingThreadSerial.put(threadSerialNum, this.mapping.get(threadNameStringId));
        }
    }

    @Override
    public void endThread(int threadSerialNum) {
    }

    @Override
    public void heapDump() {
    }

    @Override
    public void heapDumpEnd() {
    }

    @Override
    public void heapDumpSegment() {
    }

    @Override
    public void cpuSamples(int totalNumOfSamples, CPUSample[] samples) {
    }

    @Override
    public void controlSettings(int bitMaskFlags, short stackTraceDepth) {
    }

    @Override
    public void rootUnknown(long objId) {
    }

    @Override
    public void rootJNIGlobal(long objId, long JNIGlobalRefId) {
    }

    @Override
    public void rootJNILocal(long objId, int threadSerialNum, int frameNum) {
        this.jniObjectIds.add(objId);
    }

    @Override
    public void rootJavaFrame(long objId, int threadSerialNum, int frameNum) {
    }

    @Override
    public void rootNativeStack(long objId, int threadSerialNum) {
    }

    @Override
    public void rootStickyClass(long objId) {
    }

    @Override
    public void rootThreadBlock(long objId, int threadSerialNum) {
    }

    @Override
    public void rootMonitorUsed(long objId) {
    }

    @Override
    public void rootThreadObj(long objId, int threadSerialNum, int stackTraceSerialNum) {
    }

    @Override
    public void classDump(long classObjId, int stackTraceSerialNum, long superClassObjId, long classLoaderObjId, long signersObjId, long protectionDomainObjId, long reserved1, long reserved2, int instanceSize, Constant[] constants, Static[] statics, InstanceField[] instanceFields) {
    }

    @Override
    public void instanceDump(long objId, int stackTraceSerialNum, long classObjId, Value<?>[] instanceFieldValues) {
        if (this.jniObjectIds.contains(classObjId)) {
            // empty if block
        }
    }

    @Override
    public void objArrayDump(long objId, int stackTraceSerialNum, long elemClassObjId, long[] elems) {
    }

    @Override
    public void primArrayDump(long objId, int stackTraceSerialNum, byte elemType, Value<?>[] elems) {
    }

    @Override
    public void finished() {
    }

    public void clean() {
        this.mapping.clear();
        this.mapping = null;
        this.classes.clear();
        this.classes = null;
        this.jniObjectIds.clear();
        this.jniObjectIds = null;
        this.mappingClass.clear();
        this.mappingClass = null;
        this.mappingThreadSerial.clear();
        this.mappingThreadSerial = null;
        this.stackMap.clear();
        this.stackMap = null;
    }
}
