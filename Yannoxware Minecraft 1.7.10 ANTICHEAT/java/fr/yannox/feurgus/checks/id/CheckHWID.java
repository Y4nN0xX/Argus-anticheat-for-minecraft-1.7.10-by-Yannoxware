package fr.yannox.feurgus.checks.id;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutHWID;
import fr.yannox.feurgus.utils.threading.ThreadStarter;

public class CheckHWID
extends ACheck {
    public CheckHWID(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            this.sendPacket(new PacketOutHWID());
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getRepeatTime() {
        return 270;
    }
}
