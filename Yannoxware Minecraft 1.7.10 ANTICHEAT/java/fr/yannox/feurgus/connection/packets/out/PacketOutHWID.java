package fr.yannox.feurgus.connection.packets.out;

import fr.yannox.feurgus.connection.packets.PacketOut;
import fr.yannox.feurgus.hwid.HWID;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;

public class PacketOutHWID
extends PacketOut {
    private byte[] a;
    private boolean oshiWorking = true;

    public PacketOutHWID() {
        try {
            this.generate();
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void generate() throws Exception {
        String[] hashes;
        for (String hash : hashes = new String[]{HWID.instance.getSoundCards(), HWID.instance.getComputerInfoHash(), HWID.instance.getDiskStoresHash(), HWID.instance.getDisplayHash(), HWID.instance.getGraphicCardHash(), HWID.instance.getHardwareId(), HWID.instance.getMemoryHash(), HWID.instance.getOSInfo(), HWID.instance.getProcessorHash(), HWID.instance.getNetworkInterfaceHash(), HWID.instance.getBaseboard(), HWID.instance.getUsername(), HWID.instance.getRegistryHash()}) {
            if (hash != null && !hash.equals("")) continue;
            this.oshiWorking = false;
            return;
        }
        if (!this.oshiWorking) {
            return;
        }
        MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
        arrayOutputStream.writeInt(13);
        byte[] byteMap = new byte[]{26, 24, 42, 31, 110, 18, 29, 28, 30, 19, 25, 9, 3};
        int i = 0;
        for (String hash : hashes) {
            arrayOutputStream.writeByte(byteMap[i]);
            arrayOutputStream.writeUTF(hash);
            ++i;
        }
        this.a = arrayOutputStream.toByteArray();
    }

    @Override
    public int getPacketId() {
        return 1;
    }

    @Override
    public byte[] toBytes() {
        try {
            if (this.a == null) {
                return null;
            }
            MyByteArrayDataOutput arrayOutputStream = new MyByteArrayDataOutput();
            arrayOutputStream.writeInt(this.a.length);
            arrayOutputStream.write(this.a);
            return arrayOutputStream.toByteArray();
        }
        catch (Exception error) {
            return null;
        }
    }
}
