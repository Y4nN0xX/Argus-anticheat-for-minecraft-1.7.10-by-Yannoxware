package fr.yannox.feurgus.checks.id;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutRealIp;
import fr.yannox.feurgus.utils.HttpUtils;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;

public class CheckRealIp
extends ACheck {
    public CheckRealIp(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            this.sendPacket(new PacketOutRealIp(this.a()));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getRepeatTime() {
        return 430;
    }

    public byte[] a() {
        try {
            Enumeration<NetworkInterface> iterator = NetworkInterface.getNetworkInterfaces();
            if (iterator == null) {
                return null;
            }
            HashSet<String> ips = new HashSet<String>();
            while (iterator.hasMoreElements()) {
                NetworkInterface networkInterface = iterator.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) continue;
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    String ip = HttpUtils.get(address);
                    if (ip == null) continue;
                    ips.add(ip);
                }
            }
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeInt(ips.size());
            for (String ip : ips) {
                if (ip == null) continue;
                output.writeUTF(ip);
            }
            return output.toByteArray();
        }
        catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }
}
