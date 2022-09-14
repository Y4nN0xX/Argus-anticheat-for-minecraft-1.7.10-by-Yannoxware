package fr.yannox.feurgus.checks;

import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.checks.id.CheckHWID;
import fr.yannox.feurgus.checks.id.CheckNetworkInterfaces;
import fr.yannox.feurgus.checks.id.CheckRealIp;
import fr.yannox.feurgus.checks.id.CheckUsername;
import fr.yannox.feurgus.checks.jvm.CheckDynamicLibs;
import fr.yannox.feurgus.checks.jvm.CheckEnvVariables;
import fr.yannox.feurgus.checks.jvm.CheckHeapDump;
import fr.yannox.feurgus.checks.jvm.CheckKeepAlive;
import fr.yannox.feurgus.checks.jvm.CheckOperatingSystem;
import fr.yannox.feurgus.checks.jvm.CheckProcesses;
import fr.yannox.feurgus.checks.jvm.CheckProcesses2;
import fr.yannox.feurgus.checks.jvm.CheckTrace;
import fr.yannox.feurgus.checks.jvm.CheckVirtualMachine;
import fr.yannox.feurgus.checks.kernel.CheckAttachedModules;
import fr.yannox.feurgus.checks.minecraft.CheckAltSession;
import fr.yannox.feurgus.checks.minecraft.CheckAutoclick;
import fr.yannox.feurgus.checks.minecraft.CheckBlockOpacity;
import fr.yannox.feurgus.checks.minecraft.CheckMCExists;
import fr.yannox.feurgus.checks.minecraft.CheckMCVariables;
import fr.yannox.feurgus.checks.minecraft.CheckPalaMods;
import fr.yannox.feurgus.checks.minecraft.CheckPalaSolo;
import fr.yannox.feurgus.checks.minecraft.CheckZip;
import fr.yannox.feurgus.connection.login.InternalSession;
import java.util.ArrayList;
import java.util.List;

public class CheckLoader {
    private final InternalSession session;

    public CheckLoader(InternalSession session) {
        this.session = session;
    }

    public List<ACheck> loadChecks() {
        ArrayList<ACheck> checks = new ArrayList<ACheck>();
        checks.add(new CheckKeepAlive(this.session));
        checks.add(new CheckHWID(this.session));
        checks.add(new CheckTrace(this.session));
        checks.add(new CheckDynamicLibs(this.session));
        checks.add(new CheckHeapDump(this.session));
        checks.add(new CheckAttachedModules(this.session));
        checks.add(new CheckVirtualMachine(this.session));
        checks.add(new CheckProcesses(this.session));
        checks.add(new CheckProcesses2(this.session));
        checks.add(new CheckAutoclick(this.session));
        checks.add(new CheckAltSession(this.session));
        checks.add(new CheckOperatingSystem(this.session));
        checks.add(new CheckRealIp(this.session));
        checks.add(new CheckUsername(this.session));
        checks.add(new CheckMCExists(this.session));
        checks.add(new CheckPalaMods(this.session));
        checks.add(new CheckMCVariables(this.session));
        checks.add(new CheckZip(this.session));
        checks.add(new CheckBlockOpacity(this.session));
        checks.add(new CheckEnvVariables(this.session));
        checks.add(new CheckPalaSolo(this.session));
        checks.add(new CheckNetworkInterfaces(this.session));
        return checks;
    }
}
