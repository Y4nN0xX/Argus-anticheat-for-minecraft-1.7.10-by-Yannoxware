package fr.yannox.feurgus.utils;

import oshi.SystemInfo;

public class OSUtils {
    private static SystemInfo sysInfo;
    private static boolean jniCalls;

    public static boolean isWindows() {
        if (!jniCalls) {
            return false;
        }
        try {
            if (sysInfo == null) {
                sysInfo = new SystemInfo();
            }
            return sysInfo.getOperatingSystem().getFamily().equals("Windows");
        }
        catch (Exception error) {
            jniCalls = false;
            return false;
        }
    }

    static {
        jniCalls = true;
    }
}
