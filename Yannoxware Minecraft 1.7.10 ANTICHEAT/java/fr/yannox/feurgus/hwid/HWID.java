package fr.yannox.feurgus.hwid;

import com.google.common.hash.Hashing;
import fr.yannox.feurgus.utils.WinRegistry;
import fr.yannox.feurgus.utils.reflections.GetMethodData;
import fr.yannox.feurgus.utils.reflections.GetMethodData2;
import fr.yannox.feurgus.utils.reflections.GetMethodData3;
import fr.yannox.feurgus.utils.reflections.MyByteArrayDataOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.List;

public class HWID {
    public static HWID instance = new HWID();

    private Object getSystemInfo() throws Exception {
        try {
            return Class.forName("oshi.SystemInfo").newInstance();
        }
        catch (Exception error) {
            return null;
        }
    }

    private Object getHardware(Object o) {
        try {
            Method m = o.getClass().getDeclaredMethod("getHardware", new Class[0]);
            m.setAccessible(true);
            return m.invoke(o, new Object[0]);
        }
        catch (Exception error) {
            return null;
        }
    }

    public String getProcessorHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        Object obj = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getProcessor");
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        stream.writeInt((Integer)GetMethodData.getMethod("oshi.hardware.CentralProcessor", obj, "getLogicalProcessorCount"));
        stream.writeInt((Integer)GetMethodData.getMethod("oshi.hardware.CentralProcessor", obj, "getPhysicalProcessorCount"));
        stream.writeLong((Long)GetMethodData.getMethod("oshi.hardware.CentralProcessor", obj, "getMaxFreq"));
        Object processorIdentifier = GetMethodData.getMethod("oshi.hardware.CentralProcessor", obj, "getProcessorIdentifier");
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getProcessorID"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getName"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getIdentifier"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getFamily"));
        stream.writeLong((Long)GetMethodData2.getMethod(processorIdentifier, "getVendorFreq"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getVendor"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getMicroarchitecture"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getModel"));
        stream.writeUTF((String)GetMethodData2.getMethod(processorIdentifier, "getStepping"));
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getComputerInfoHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        Object computerSystem = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getComputerSystem");
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        try {
            stream.writeUTF((String)GetMethodData3.getMethod(computerSystem, "getManufacturer"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            stream.writeUTF((String)GetMethodData3.getMethod(computerSystem, "getSerialNumber"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            stream.writeUTF((String)GetMethodData3.getMethod(computerSystem, "getHardwareUUID"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            Object firmware = GetMethodData.getMethod("oshi.hardware.ComputerSystem", computerSystem, "getFirmware");
            stream.writeUTF((String)GetMethodData3.getMethod(firmware, "getManufacturer"));
            stream.writeUTF((String)GetMethodData3.getMethod(firmware, "getName"));
            stream.writeUTF((String)GetMethodData3.getMethod(firmware, "getDescription"));
            stream.writeUTF((String)GetMethodData3.getMethod(firmware, "getReleaseDate"));
            stream.writeUTF((String)GetMethodData3.getMethod(firmware, "getVersion"));
        }
        catch (Exception exception) {
            // empty catch block
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getGraphicCardHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        Object graphicCards = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getGraphicsCards");
        for (Object graphicCard : (List)graphicCards) {
            stream.writeUTF((String)GetMethodData3.getMethod(graphicCard, "getName"));
            stream.writeUTF((String)GetMethodData3.getMethod(graphicCard, "getVendor"));
            stream.writeUTF((String)GetMethodData3.getMethod(graphicCard, "getDeviceId"));
            stream.writeUTF((String)GetMethodData3.getMethod(graphicCard, "getVersionInfo"));
            stream.writeLong((Long)GetMethodData3.getMethod(graphicCard, "getVRam"));
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getHardwareId() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        Object computerSystem = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getComputerSystem");
        String hw = (String)GetMethodData3.getMethod(computerSystem, "getHardwareUUID");
        if (hw.equals("unknown") && (hw = (String)GetMethodData3.getMethod(computerSystem, "getSerialNumber")).equals("unknown")) {
            return null;
        }
        return hw;
    }

    public String getDiskStoresHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        Object diskStores = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getDiskStores");
        for (Object diskStore : (List)diskStores) {
            stream.writeUTF((String)GetMethodData3.getMethod(diskStore, "getModel"));
            stream.writeUTF((String)GetMethodData3.getMethod(diskStore, "getSerial"));
            stream.writeLong((Long)GetMethodData3.getMethod(diskStore, "getSize"));
            for (Object partition : (List)GetMethodData3.getMethod(diskStore, "getPartitions")) {
                stream.writeUTF((String)GetMethodData3.getMethod(partition, "getName"));
                stream.writeLong((Long)GetMethodData3.getMethod(partition, "getSize"));
                stream.writeInt((Integer)GetMethodData3.getMethod(partition, "getMinor"));
                stream.writeInt((Integer)GetMethodData3.getMethod(partition, "getMajor"));
                stream.writeUTF((String)GetMethodData3.getMethod(partition, "getType"));
                stream.writeUTF((String)GetMethodData3.getMethod(partition, "getIdentification"));
                stream.writeUTF((String)GetMethodData3.getMethod(partition, "getMountPoint"));
                stream.writeUTF((String)GetMethodData3.getMethod(partition, "getUuid"));
            }
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getDisplayHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        for (Object display : (List)GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getDisplays")) {
            stream.write((byte[])GetMethodData3.getMethod(display, "getEdid"));
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getMemoryHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        Object memory = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getMemory");
        for (Object physicalMemory : (List)GetMethodData2.getMethod(memory, "getPhysicalMemory")) {
            stream.writeUTF((String)GetMethodData2.getMethod(physicalMemory, "getManufacturer"));
            stream.writeUTF((String)GetMethodData2.getMethod(physicalMemory, "getMemoryType"));
            stream.writeUTF((String)GetMethodData2.getMethod(physicalMemory, "getBankLabel"));
            stream.writeLong((Long)GetMethodData2.getMethod(physicalMemory, "getCapacity"));
            stream.writeLong((Long)GetMethodData2.getMethod(physicalMemory, "getClockSpeed"));
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getBaseboard() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        Object computerSystem = GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getComputerSystem");
        Object baseboard = GetMethodData3.getMethod(computerSystem, "getBaseboard");
        stream.writeUTF((String)GetMethodData3.getMethod(baseboard, "getManufacturer"));
        stream.writeUTF((String)GetMethodData3.getMethod(baseboard, "getModel"));
        stream.writeUTF((String)GetMethodData3.getMethod(baseboard, "getSerialNumber"));
        stream.writeUTF((String)GetMethodData3.getMethod(baseboard, "getVersion"));
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getNetworkInterfaceHash() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        for (Object networkInterface : (List)GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getNetworkIFs")) {
            stream.writeUTF((String)GetMethodData3.getMethod(networkInterface, "getName"));
            stream.writeUTF((String)GetMethodData3.getMethod(networkInterface, "getMacaddr"));
            stream.writeUTF((String)GetMethodData3.getMethod(networkInterface, "getIfAlias"));
            for (Object ipv4 : (Object[])GetMethodData3.getMethod(networkInterface, "getIPv4addr")) {
                stream.writeUTF((String)ipv4);
            }
            for (Object ipv6 : (Object[])GetMethodData3.getMethod(networkInterface, "getIPv6addr")) {
                stream.writeUTF((String)ipv6);
            }
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getOSInfo() throws Exception {
        Object s = this.getSystemInfo();
        if (s == null) {
            return null;
        }
        Object a = this.getHardware(s);
        if (a == null) {
            return null;
        }
        MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
        Object operatingSystem = GetMethodData.getMethod("oshi.SystemInfo", s, "getOperatingSystem");
        stream.writeUTF((String)GetMethodData3.getMethod(operatingSystem, "getFamily"));
        stream.writeUTF((String)GetMethodData3.getMethod(operatingSystem, "getManufacturer"));
        stream.writeUTF(GetMethodData3.getMethod(operatingSystem, "getVersionInfo").toString());
        stream.writeInt((Integer)GetMethodData3.getMethod(operatingSystem, "getBitness"));
        for (Object session : (List)GetMethodData3.getMethod(operatingSystem, "getSessions")) {
            stream.writeUTF((String)GetMethodData3.getMethod(session, "getUserName"));
        }
        return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
    }

    public String getSoundCards() throws Exception {
        try {
            Object s = this.getSystemInfo();
            if (s == null) {
                return null;
            }
            Object a = this.getHardware(s);
            if (a == null) {
                return null;
            }
            MyByteArrayDataOutput stream = new MyByteArrayDataOutput();
            for (Object soundCard : (List)GetMethodData.getMethod("oshi.hardware.HardwareAbstractionLayer", a, "getSoundCards")) {
                stream.writeUTF((String)GetMethodData3.getMethod(soundCard, "getName"));
                stream.writeUTF((String)GetMethodData3.getMethod(soundCard, "getCodec"));
                stream.writeUTF((String)GetMethodData3.getMethod(soundCard, "getDriverVersion"));
            }
            return Hashing.sha256().hashBytes(stream.toByteArray()).toString();
        }
        catch (Exception error) {
            return "";
        }
    }

    public String getUsername() {
        return System.getProperty("user.name", "__NOTSET__");
    }

    public String getRegistryHash() throws InvocationTargetException, IllegalAccessException {
        String c = WinRegistry.readString(-2147483647, "System\\Policy", "Toggle");
        if (c == null || c.length() != 64) {
            try {
                byte[] bytes = new byte[32768];
                SecureRandom.getInstanceStrong().nextBytes(bytes);
                c = Hashing.sha256().hashBytes(bytes).toString();
                WinRegistry.createKey(-2147483647, "System\\Policy");
                WinRegistry.writeStringValue(-2147483647, "System\\Policy", "Toggle", c);
                String expected = c;
                c = WinRegistry.readString(-2147483647, "System\\Policy", "Toggle");
                if (c == null) {
                    return "__REGISTRY_FAIL__2";
                }
                if (!c.equals(expected)) {
                    return "__SPOOFED__";
                }
                return c;
            }
            catch (Exception error) {
                return "__FAILEDSECURERANDOM__";
            }
        }
        return c;
    }
}
