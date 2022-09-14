package fr.yannox.feurgus.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

public class DetectVM {
    private static final Map<String, String> vmMacAddressProps = new HashMap<String, String>();
    private static final Map<String, String> vmVendor = new HashMap<String, String>();
    private static final String[] vmModelArray;

    public static String identifyVM() {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hw = si.getHardware();
            String vendor = hw.getProcessor().getProcessorIdentifier().getVendor().trim();
            if (vmVendor.containsKey(vendor)) {
                return vmVendor.get(vendor);
            }
            List nifs = hw.getNetworkIFs();
            for (String[] nif : nifs) {
                String mac = nif.getMacaddr().toUpperCase();
                String oui = mac.length() > 7 ? mac.substring(0, 8) : mac;
                if (!vmMacAddressProps.containsKey(oui)) continue;
                return vmMacAddressProps.get(oui);
            }
            String model = hw.getComputerSystem().getModel();
            for (String vm : vmModelArray) {
                if (!model.contains(vm)) continue;
                return vm;
            }
            String manufacturer = hw.getComputerSystem().getManufacturer();
            if ("Microsoft Corporation".equals(manufacturer) && "Virtual Machine".equals(model)) {
                return "Microsoft Hyper-V";
            }
            return "";
        }
        catch (Exception error) {
            return "JNA calls blocked";
        }
    }

    static {
        vmVendor.put("bhyve bhyve", "bhyve");
        vmVendor.put("KVMKVMKVM", "KVM");
        vmVendor.put("TCGTCGTCGTCG", "QEMU");
        vmVendor.put("Microsoft Hv", "Microsoft Hyper-V or Windows Virtual PC");
        vmVendor.put("lrpepyh vr", "Parallels");
        vmVendor.put("VMwareVMware", "VMware");
        vmVendor.put("XenVMMXenVMM", "Xen HVM");
        vmVendor.put("ACRNACRNACRN", "Project ACRN");
        vmVendor.put("QNXQVMBSQG", "QNX Hypervisor");
        vmMacAddressProps.put("00\\:50\\:56", "VMware ESX 3");
        vmMacAddressProps.put("00\\:0C\\:29", "VMware ESX 3");
        vmMacAddressProps.put("00\\:05\\:69", "VMware ESX 3");
        vmMacAddressProps.put("00\\:03\\:FF", "Microsoft Hyper-V");
        vmMacAddressProps.put("00\\:1C\\:42", "Parallels Desktop");
        vmMacAddressProps.put("00\\:0F\\:4B", "Virtual Iron 4");
        vmMacAddressProps.put("00\\:16\\:3E", "Xen or Oracle VM");
        vmMacAddressProps.put("08\\:00\\:27", "VirtualBox");
        vmMacAddressProps.put("02\\:42\\:AC", "Docker Container");
        vmModelArray = new String[]{"Linux KVM", "Linux lguest", "OpenVZ", "Qemu", "Microsoft Virtual PC", "VMWare", "linux-vserver", "Xen", "FreeBSD Jail", "VirtualBox", "Parallels", "Linux Containers", "LXC"};
    }
}
