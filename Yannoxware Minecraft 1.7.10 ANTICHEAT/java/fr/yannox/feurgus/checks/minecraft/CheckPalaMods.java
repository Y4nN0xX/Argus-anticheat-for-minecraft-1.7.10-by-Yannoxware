package fr.yannox.feurgus.checks.minecraft;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutPalaMods;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.zip.CRC32;

public class CheckPalaMods
extends ACheck {
    public CheckPalaMods(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        byte[] crc = this.getModCrc();
        if (crc == null) {
            return;
        }
        this.sendPacket(new PacketOutPalaMods(crc));
    }

    private byte[] getModCrc() {
        File appdata = this.getAppData();
        if (!appdata.exists()) {
            return null;
        }
        File yannoxFolder = new File(appdata, ".yannox");
        if (!yannoxFolder.exists() || !yannoxFolder.isDirectory()) {
            return null;
        }
        File modFolder = new File(yannoxFolder, "mods");
        if (!modFolder.exists() || !modFolder.isDirectory()) {
            return null;
        }
        File minageZip = new File(modFolder, "Minage.zip");
        if (minageZip.exists()) {
            try {
                minageZip.delete();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        ArrayList<AbstractMap.SimpleEntry<String, String>> list = new ArrayList<AbstractMap.SimpleEntry<String, String>>();
        for (File file : Objects.requireNonNull(modFolder.listFiles())) {
            String crc;
            if (!file.isFile()) continue;
            String fileName = file.getName();
            try {
                crc = this.getCRC32(file);
            }
            catch (Exception err) {
                continue;
            }
            if (crc == null) continue;
            list.add(new AbstractMap.SimpleEntry<String, String>(fileName, crc));
        }
        output.writeByte(19);
        output.writeInt(list.size());
        output.writeByte(43);
        for (Map.Entry entry : list) {
            output.writeByte(28);
            output.writeUTF((String)entry.getKey());
            output.writeByte(20);
            output.writeUTF((String)entry.getValue());
        }
        return output.toByteArray();
    }

    private String getCRC32(File file) throws Exception {
        int bytesRead;
        FileInputStream in = new FileInputStream(file);
        CRC32 crcMaker = new CRC32();
        int size = 65536;
        byte[] buffer = new byte[65536];
        while ((bytesRead = ((InputStream)in).read(buffer)) != -1) {
            crcMaker.update(buffer, 0, bytesRead);
        }
        long value = crcMaker.getValue();
        return String.format(Locale.US, "%08X", value);
    }

    private File getAppData() {
        String workingDirectory;
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN")) {
            workingDirectory = System.getenv("AppData");
        } else {
            workingDirectory = System.getProperty("user.home");
            workingDirectory = workingDirectory + "/Library/Application Support";
        }
        return new File(workingDirectory);
    }

    @Override
    public int getRepeatTime() {
        return 300;
    }
}
