package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutPalaSolo;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckPalaSolo
extends ACheck {
    public CheckPalaSolo(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        Map<String, Long> entries = this.getSoloEntries();
        if (entries == null || entries.isEmpty()) {
            return;
        }
        this.sendPacket(new PacketOutPalaSolo(entries));
    }

    private Map<String, Long> getSoloEntries() {
        File appdata = this.getAppData();
        if (!appdata.exists()) {
            return null;
        }
        File atomFolder = new File(appdata, ".Atom");
        if (!atomFolder.exists() || !atomFolder.isDirectory()) {
            return null;
        }
        HashMap<String, Long> entries = new HashMap<String, Long>();
        entries.put(".Atom", CheckPalaSolo.getLastModified(atomFolder).getTime());
        return entries;
    }

    private static Date getLastModified(File directory) {
        File[] files = directory.listFiles();
        if (files.length == 0) {
            return new Date(directory.lastModified());
        }
        Arrays.sort(files, (o1, o2) -> Long.compare(o2.lastModified(), o1.lastModified()));
        return new Date(files[0].lastModified());
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
        return 287;
    }
}
