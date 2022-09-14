import fr.yannox.feurgus.connection.MainThread;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.keys.z;

public class u
extends Thread {
    public static String a() {
        new u();
        return z.q();
    }

    public u() {
        new MainThread();
    }

    public static void z() {
        if (InternalSession.instance == null) {
            return;
        }
        InternalSession.instance.stop();
    }
}
