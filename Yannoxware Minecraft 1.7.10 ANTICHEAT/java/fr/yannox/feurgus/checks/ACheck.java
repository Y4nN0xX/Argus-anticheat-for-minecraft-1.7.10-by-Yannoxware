package fr.yannox.feurgus.checks;

import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.PacketOut;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ACheck
extends Thread {
    private AtomicBoolean loaded;
    private long occurrences = 0L;
    private final InternalSession session;

    public ACheck(InternalSession session) {
        this.session = session;
        this.loaded = new AtomicBoolean(true);
    }

    @Override
    public void run() {
        while (this.loaded.get()) {
            this.runCheck(this.occurrences);
            if ((long)this.getRepeatTime() <= 0L) continue;
            try {
                Thread.sleep((long)this.getRepeatTime() * 1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            ++this.occurrences;
        }
    }

    public abstract void runCheck(long var1);

    public abstract int getRepeatTime();

    protected void sendPacket(Object object) {
        try {
            if (this.session == null) {
                return;
            }
            this.session.sendPacket((PacketOut)object);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public InternalSession getSession() {
        return this.session;
    }

    public void unload() {
        this.loaded.set(false);
    }
}
