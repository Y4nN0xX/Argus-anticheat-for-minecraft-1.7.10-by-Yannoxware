package fr.yannox.feurgus.utils.heap;

import fr.yannox.feurgus.utils.heap.datastructures.AllocSite;
import fr.yannox.feurgus.utils.heap.datastructures.CPUSample;
import fr.yannox.feurgus.utils.heap.datastructures.Constant;
import fr.yannox.feurgus.utils.heap.datastructures.InstanceField;
import fr.yannox.feurgus.utils.heap.datastructures.Static;
import fr.yannox.feurgus.utils.heap.datastructures.Value;

public interface RecordHandler {
    public void header(String var1, int var2, long var3);

    public void stringInUTF8(long var1, String var3);

    public void loadClass(int var1, long var2, int var4, long var5);

    public void unloadClass(int var1);

    public void stackFrame(long var1, long var3, long var5, long var7, int var9, int var10);

    public void stackTrace(int var1, int var2, int var3, long[] var4);

    public void allocSites(short var1, float var2, int var3, int var4, long var5, long var7, AllocSite[] var9);

    public void heapSummary(int var1, int var2, long var3, long var5);

    public void startThread(int var1, long var2, int var4, long var5, long var7, long var9);

    public void endThread(int var1);

    public void heapDump();

    public void heapDumpEnd();

    public void heapDumpSegment();

    public void cpuSamples(int var1, CPUSample[] var2);

    public void controlSettings(int var1, short var2);

    public void rootUnknown(long var1);

    public void rootJNIGlobal(long var1, long var3);

    public void rootJNILocal(long var1, int var3, int var4);

    public void rootJavaFrame(long var1, int var3, int var4);

    public void rootNativeStack(long var1, int var3);

    public void rootStickyClass(long var1);

    public void rootThreadBlock(long var1, int var3);

    public void rootMonitorUsed(long var1);

    public void rootThreadObj(long var1, int var3, int var4);

    public void classDump(long var1, int var3, long var4, long var6, long var8, long var10, long var12, long var14, int var16, Constant[] var17, Static[] var18, InstanceField[] var19);

    public void instanceDump(long var1, int var3, long var4, Value<?>[] var6);

    public void objArrayDump(long var1, int var3, long var4, long[] var6);

    public void primArrayDump(long var1, int var3, byte var4, Value<?>[] var5);

    public void finished();
}
