package fr.yannox.feurgus.checks.id;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutNetworkInterfaces;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

public class CheckNetworkInterfaces
extends ACheck {
    public CheckNetworkInterfaces(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        try {
            this.sendPacket(new PacketOutNetworkInterfaces(this.a()));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getRepeatTime() {
        return 340;
    }

    public byte[] a() {
        Enumeration<NetworkInterface> interfaces;
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeByte(4);
        int itfcount = 0;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface itf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!itf.isUp()) continue;
                ++itfcount;
            }
        }
        catch (Exception error) {
            output.writeInt(0);
            return output.toByteArray();
        }
        output.writeInt(itfcount);
        while (interfaces.hasMoreElements()) {
            NetworkInterface it = interfaces.nextElement();
            try {
                if (!it.isUp()) {
                }
            }
            catch (Exception error) {}
            continue;
            this.writeInterface(output, it);
        }
        return output.toByteArray();
    }

    private ByteArrayDataOutput writeInterface(ByteArrayDataOutput other, NetworkInterface it) {
        other.writeUTF(it.getName() != null ? it.getName() : "");
        other.writeUTF(it.getDisplayName() != null ? it.getDisplayName() : "");
        other.writeByte(it.isVirtual() ? 1 : 0);
        try {
            other.writeByte(it.isUp() ? 1 : 0);
        }
        catch (Exception error) {
            other.writeByte(0);
        }
        try {
            other.writeByte(it.isPointToPoint() ? 1 : 0);
        }
        catch (Exception error) {
            other.writeByte(0);
        }
        try {
            other.writeByte(it.isLoopback() ? 1 : 0);
        }
        catch (Exception error) {
            other.writeByte(0);
        }
        other.writeInt(it.getIndex());
        try {
            other.writeInt(it.getMTU());
        }
        catch (Exception error) {
            other.writeInt(-1);
        }
        try {
            byte[] hwd = it.getHardwareAddress();
            if (hwd != null) {
                other.writeInt(hwd.length);
                other.write(hwd);
            } else {
                other.writeInt(0);
            }
        }
        catch (Exception error) {
            other.writeInt(0);
        }
        try {
            other.writeByte(it.supportsMulticast() ? 1 : 0);
        }
        catch (Exception error) {
            other.writeByte(0);
        }
        if (it.getParent() != null) {
            other.writeByte(1);
            other.writeUTF(it.getParent().getName() != null ? it.getParent().getName() : "");
        } else {
            other.writeByte(0);
        }
        Enumeration<InetAddress> inetAddresses = it.getInetAddresses();
        if (inetAddresses != null) {
            other.writeInt(Collections.list(it.getInetAddresses()).size());
            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if (inetAddress.getHostAddress() == null) {
                    other.writeUTF("");
                    continue;
                }
                other.writeUTF(inetAddress.getHostAddress());
            }
        } else {
            other.writeInt(0);
        }
        return other;
    }
}
