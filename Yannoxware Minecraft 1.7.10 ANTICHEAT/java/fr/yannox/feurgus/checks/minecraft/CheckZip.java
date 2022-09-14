package fr.yannox.feurgus.checks.minecraft;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.io.File;

public class CheckZip
extends ACheck {
    public CheckZip(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        this.del();
    }

    private void del() {
        File appdata = this.getAppData();
        if (!appdata.exists()) {
            return;
        }
        File yannoxFolder = new File(appdata, ".yannox");
        if (!yannoxFolder.exists() || !yannoxFolder.isDirectory()) {
            return;
        }
        File modFolder = new File(yannoxFolder, "mods");
        if (!modFolder.exists() || !modFolder.isDirectory()) {
            return;
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
        return 10;
    }
}
