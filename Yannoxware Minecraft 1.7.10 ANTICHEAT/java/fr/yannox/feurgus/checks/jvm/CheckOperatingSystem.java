package fr.yannox.feurgus.checks.jvm;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOperatingSystem;
import fr.yannox.feurgus.utils.reflections.GetMethodData;
import fr.yannox.feurgus.utils.reflections.GetMethodData3;
import fr.yannox.feurgus.utils.threading.ThreadStarter;

public class CheckOperatingSystem
extends ACheck {
    private boolean oshiWorking = true;

    public CheckOperatingSystem(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    private Object getSystemInfo() {
        try {
            return Class.forName("oshi.SystemInfo").newInstance();
        }
        catch (Exception error) {
            return null;
        }
    }

    @Override
    public void runCheck(long occurrences) {
        String family = "call jna blocked";
        if (!this.oshiWorking) {
            family = System.getProperty("os.name", "call jna blocked");
            this.sendPacket(new PacketOperatingSystem(family.getBytes()));
            return;
        }
        try {
            Object a = this.getSystemInfo();
            if (a != null) {
                Object operatingSystem = GetMethodData.getMethod("oshi.SystemInfo", a, "getOperatingSystem");
                family = (String)GetMethodData3.getMethod(operatingSystem, "getFamily");
            } else {
                this.oshiWorking = false;
                family = System.getProperty("os.name", "call jna blocked");
            }
        }
        catch (Exception error) {
            this.oshiWorking = false;
            family = System.getProperty("os.name", "call jna blocked");
        }
        this.sendPacket(new PacketOperatingSystem(family.getBytes()));
    }

    @Override
    public int getRepeatTime() {
        return 342;
    }
}
