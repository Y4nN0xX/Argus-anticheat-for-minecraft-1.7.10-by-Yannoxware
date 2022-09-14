package fr.yannox.feurgus.checks.jvm;

import com.sun.management.HotSpotDiagnosticMXBean;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.utils.heap.HprofParser;
import fr.yannox.feurgus.utils.heap.StickyRecordHandler;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.security.SecureRandom;
import javax.management.MBeanServer;

public class CheckHeapDump
extends ACheck {
    public CheckHeapDump(InternalSession session) {
        super(session);
    }

    private StickyRecordHandler dumpHeap() throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        String fileName = this.generateRandomFileName() + ".hprof";
        mxBean.dumpHeap(fileName, false);
        File file = new File(fileName);
        StickyRecordHandler recordHandler = new StickyRecordHandler();
        HprofParser parser = new HprofParser(recordHandler);
        parser.parse(file);
        file.delete();
        return recordHandler;
    }

    private String generateRandomFileName() {
        SecureRandom secureRandom = new SecureRandom();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = secureRandom.nextInt(4) + 8;
        StringBuilder random = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            random.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(secureRandom.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length())));
        }
        return random.toString();
    }

    @Override
    public void runCheck(long occurrences) {
    }

    @Override
    public int getRepeatTime() {
        return 30;
    }
}
