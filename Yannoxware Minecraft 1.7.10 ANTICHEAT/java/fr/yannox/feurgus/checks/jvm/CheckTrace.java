package fr.yannox.feurgus.checks.jvm;

import com.google.common.collect.Sets;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.MainThread;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutTrace;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CheckTrace
extends ACheck {
    private static final long TIME_BETWEEN_PACKETS = 2700L;
    private Set<Integer> threadlist = Sets.newConcurrentHashSet();
    private Set<Integer> trace = Sets.newConcurrentHashSet();
    private long lastSent;

    public CheckTrace(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        int hash;
        HashSet<String> traceDetected = new HashSet<String>();
        HashSet<String> threadsDetected = new HashSet<String>();
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            if (InternalSession.instance.getChecks().contains(entry.getKey().getClass()) || entry.getKey().getClass().equals(MainThread.thisClass)) continue;
            if (entry.getKey() != null && entry.getKey().getClass() != null) {
                String l = entry.getKey().getClass().getName();
                if (l.startsWith("jdk.") || l.startsWith("java.")) continue;
                threadsDetected.add(entry.getKey().getClass().getName());
            }
            for (StackTraceElement element : entry.getValue()) {
                String l = element.getClassName();
                if (l.startsWith("jdk.") || l.startsWith("java.")) continue;
                traceDetected.add(element.getClassName());
            }
        }
        if (System.currentTimeMillis() - this.lastSent < 2700L) {
            traceDetected.clear();
            threadsDetected.clear();
            try {
                Thread.sleep(5L);
            }
            catch (Exception exception) {
                // empty catch block
            }
            return;
        }
        HashSet<String> traceToSend = new HashSet<String>();
        HashSet<String> threadsToSend = new HashSet<String>();
        for (String t : traceDetected) {
            if (traceToSend.size() >= 250) break;
            hash = t.hashCode();
            if (this.trace.contains(hash)) continue;
            this.trace.add(hash);
            traceToSend.add(t);
        }
        for (String t : threadsDetected) {
            if (threadsToSend.size() >= 250) break;
            hash = t.hashCode();
            if (this.threadlist.contains(hash)) continue;
            this.threadlist.add(hash);
            threadsToSend.add(t);
        }
        traceDetected.clear();
        threadsDetected.clear();
        if (!traceToSend.isEmpty() || !threadsToSend.isEmpty()) {
            try {
                this.sendPacket(new PacketOutTrace(this.generateArray(traceToSend, threadsToSend)));
            }
            catch (Exception error) {
                error.printStackTrace();
            }
        }
        this.lastSent = System.currentTimeMillis();
        try {
            Thread.sleep(5L);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private byte[] generateArray(Set<String> trace, Set<String> threads) {
        MyByteArrayDataOutput output = new MyByteArrayDataOutput();
        output.write((byte)22);
        output.writeInt(trace.size());
        trace.forEach(output::writeUTF);
        output.write((byte)47);
        output.writeInt(threads.size());
        threads.forEach(output::writeUTF);
        return output.toByteArray();
    }

    @Override
    public void unload() {
        if (this.threadlist != null) {
            this.threadlist.clear();
        }
        if (this.trace != null) {
            this.trace.clear();
        }
        super.unload();
    }

    @Override
    public int getRepeatTime() {
        return 0;
    }
}
