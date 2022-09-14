package fr.yannox.feurgus.checks.id;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutUsername;
import fr.yannox.feurgus.utils.threading.ThreadStarter;

public class CheckUsername
extends ACheck {
    public CheckUsername(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        String us = System.getProperty("user.name", "__NOTSET__");
        try {
            this.sendPacket(new PacketOutUsername(us));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getRepeatTime() {
        return 219;
    }
}
