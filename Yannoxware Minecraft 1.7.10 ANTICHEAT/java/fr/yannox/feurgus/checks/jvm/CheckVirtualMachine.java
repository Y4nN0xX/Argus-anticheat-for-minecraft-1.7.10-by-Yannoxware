package fr.yannox.feurgus.checks.jvm;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.yannox.feurgus.checks.ACheck;
import fr.yannox.feurgus.connection.login.InternalSession;
import fr.yannox.feurgus.connection.packets.out.PacketOutVirtualMachine;
import fr.yannox.feurgus.utils.DetectVM;
import fr.yannox.feurgus.utils.threading.ThreadStarter;
import java.util.ArrayList;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

public class CheckVirtualMachine
extends ACheck {
    private boolean oshiWorking = true;

    public CheckVirtualMachine(InternalSession session) {
        super(session);
        ThreadStarter.start(this);
    }

    @Override
    public void runCheck(long occurrences) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        if (!this.oshiWorking) {
            return;
        }
        String virtualMachine = DetectVM.identifyVM();
        if (virtualMachine == null || virtualMachine.equals("")) {
            virtualMachine = "none";
        }
        if (virtualMachine.equals("JNA calls blocked")) {
            this.oshiWorking = false;
            return;
        }
        output.writeUTF(virtualMachine);
        List<String> preciseInfo = this.getPreciseInfo();
        if (preciseInfo == null) {
            this.oshiWorking = false;
            return;
        }
        output.writeInt(preciseInfo.size());
        for (String info : preciseInfo) {
            output.writeUTF(info);
        }
        try {
            this.sendPacket(new PacketOutVirtualMachine(output.toByteArray()));
        }
        catch (Exception error) {
            error.printStackTrace();
        }
        try {
            Thread.sleep(900000L);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
    }

    private List<String> getPreciseInfo() {
        try {
            ComputerSystem system;
            ArrayList<String> list = new ArrayList<String>();
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hw = si.getHardware();
            if (hw == null) {
                return new ArrayList<String>();
            }
            CentralProcessor centralProcessor = hw.getProcessor();
            if (centralProcessor == null) {
                return new ArrayList<String>();
            }
            CentralProcessor.ProcessorIdentifier identifier = centralProcessor.getProcessorIdentifier();
            if (identifier == null) {
                return new ArrayList<String>();
            }
            String vendor = identifier.getVendor();
            if (vendor == null) {
                return new ArrayList<String>();
            }
            vendor = vendor.trim();
            list.add(vendor);
            List nifs = hw.getNetworkIFs();
            if (nifs != null) {
                for (NetworkIF nif : nifs) {
                    String mac;
                    if (nif == null || (mac = nif.getMacaddr()) == null) continue;
                    String oui = (mac = mac.toUpperCase()).length() > 7 ? mac.substring(0, 8) : mac;
                    list.add(oui);
                }
            }
            if ((system = hw.getComputerSystem()) != null) {
                String manufacturer;
                String model = system.getModel();
                if (model != null) {
                    list.add(model);
                }
                if ((manufacturer = system.getManufacturer()) != null) {
                    list.add(manufacturer);
                }
            }
            return list;
        }
        catch (Exception error) {
            this.oshiWorking = false;
            return null;
        }
    }

    @Override
    public int getRepeatTime() {
        return 600;
    }
}
