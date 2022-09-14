package fr.yannox.feurgus.checks.jvm;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutKeepAlive;
import fr.yannox.feurgus.utils.NetworkKey;
import fr.yannox.feurgus.utils.threading.ThreadStarter;

public class CheckKeepAlive
extends ACheck {
    public CheckKeepAlive(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        InternalSession session = this.getSession();
        NetworkKey networkKey = session.networkKey;
        networkKey.generateKeepAliveKey();
        this.sendPacket(new PacketOutKeepAlive(networkKey.getKeepAliveKey()));
    }

    @Override
    public int getRepeatTime() {
        return 8;
    }
}
