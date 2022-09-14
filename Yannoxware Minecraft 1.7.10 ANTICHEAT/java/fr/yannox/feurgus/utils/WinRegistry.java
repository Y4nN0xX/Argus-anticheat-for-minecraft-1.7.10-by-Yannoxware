package fr.yannox.feurgus.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class WinRegistry {
    public static final int HKEY_CURRENT_USER = -2147483647;
    public static final int HKEY_LOCAL_MACHINE = -2147483646;
    public static final int REG_SUCCESS = 0;
    public static final int REG_NOTFOUND = 2;
    public static final int REG_ACCESSDENIED = 5;
    private static final int KEY_ALL_ACCESS = 983103;
    private static final int KEY_READ = 131097;
    private static final Preferences userRoot = Preferences.userRoot();
    private static final Preferences systemRoot = Preferences.systemRoot();
    private static final Class<? extends Preferences> userClass = userRoot.getClass();
    private static final Method regOpenKey;
    private static final Method regCloseKey;
    private static final Method regQueryValueEx;
    private static final Method regEnumValue;
    private static final Method regQueryInfoKey;
    private static final Method regEnumKeyEx;
    private static final Method regCreateKeyEx;
    private static final Method regSetValueEx;
    private static final Method regDeleteKey;
    private static final Method regDeleteValue;

    private WinRegistry() {
    }

    public static String readString(int hkey, String key, String valueName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == -2147483646) {
            return WinRegistry.readString(systemRoot, hkey, key, valueName);
        }
        if (hkey == -2147483647) {
            return WinRegistry.readString(userRoot, hkey, key, valueName);
        }
        throw new IllegalArgumentException("hkey=" + hkey);
    }

    public static Map<String, String> readStringValues(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == -2147483646) {
            return WinRegistry.readStringValues(systemRoot, hkey, key);
        }
        if (hkey == -2147483647) {
            return WinRegistry.readStringValues(userRoot, hkey, key);
        }
        throw new IllegalArgumentException("hkey=" + hkey);
    }

    public static List<String> readStringSubKeys(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == -2147483646) {
            return WinRegistry.readStringSubKeys(systemRoot, hkey, key);
        }
        if (hkey == -2147483647) {
            return WinRegistry.readStringSubKeys(userRoot, hkey, key);
        }
        throw new IllegalArgumentException("hkey=" + hkey);
    }

    public static void createKey(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] ret;
        if (hkey == -2147483646) {
            ret = WinRegistry.createKey(systemRoot, hkey, key);
            regCloseKey.invoke(systemRoot, new Integer(ret[0]));
        } else if (hkey == -2147483647) {
            ret = WinRegistry.createKey(userRoot, hkey, key);
            regCloseKey.invoke(userRoot, new Integer(ret[0]));
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
        if (ret[1] != 0) {
            throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
        }
    }

    public static void writeStringValue(int hkey, String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (hkey == -2147483646) {
            WinRegistry.writeStringValue(systemRoot, hkey, key, valueName, value);
        } else if (hkey == -2147483647) {
            WinRegistry.writeStringValue(userRoot, hkey, key, valueName, value);
        } else {
            throw new IllegalArgumentException("hkey=" + hkey);
        }
    }

    public static void deleteKey(int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int rc = -1;
        if (hkey == -2147483646) {
            rc = WinRegistry.deleteKey(systemRoot, hkey, key);
        } else if (hkey == -2147483647) {
            rc = WinRegistry.deleteKey(userRoot, hkey, key);
        }
        if (rc != 0) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
        }
    }

    public static void deleteValue(int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int rc = -1;
        if (hkey == -2147483646) {
            rc = WinRegistry.deleteValue(systemRoot, hkey, key, value);
        } else if (hkey == -2147483647) {
            rc = WinRegistry.deleteValue(userRoot, hkey, key, value);
        }
        if (rc != 0) {
            throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
        }
    }

    private static int deleteValue(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] handles = (int[])regOpenKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key), new Integer(983103));
        if (handles[1] != 0) {
            return handles[1];
        }
        int rc = (Integer)regDeleteValue.invoke(root, new Integer(handles[0]), WinRegistry.toCstr(value));
        regCloseKey.invoke(root, new Integer(handles[0]));
        return rc;
    }

    private static int deleteKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int rc = (Integer)regDeleteKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key));
        return rc;
    }

    private static String readString(Preferences root, int hkey, String key, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] handles = (int[])regOpenKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key), new Integer(131097));
        if (handles[1] != 0) {
            return null;
        }
        byte[] valb = (byte[])regQueryValueEx.invoke(root, new Integer(handles[0]), WinRegistry.toCstr(value));
        regCloseKey.invoke(root, new Integer(handles[0]));
        return valb != null ? new String(valb).trim() : null;
    }

    private static Map<String, String> readStringValues(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        HashMap<String, String> results = new HashMap<String, String>();
        int[] handles = (int[])regOpenKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key), new Integer(131097));
        if (handles[1] != 0) {
            return null;
        }
        int[] info = (int[])regQueryInfoKey.invoke(root, new Integer(handles[0]));
        int count = info[0];
        int maxlen = info[3];
        for (int index = 0; index < count; ++index) {
            byte[] name = (byte[])regEnumValue.invoke(root, new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1));
            String value = WinRegistry.readString(hkey, key, new String(name));
            results.put(new String(name).trim(), value);
        }
        regCloseKey.invoke(root, new Integer(handles[0]));
        return results;
    }

    private static List<String> readStringSubKeys(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ArrayList<String> results = new ArrayList<String>();
        int[] handles = (int[])regOpenKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key), new Integer(131097));
        if (handles[1] != 0) {
            return null;
        }
        int[] info = (int[])regQueryInfoKey.invoke(root, new Integer(handles[0]));
        int count = info[0];
        int maxlen = info[3];
        for (int index = 0; index < count; ++index) {
            byte[] name = (byte[])regEnumKeyEx.invoke(root, new Integer(handles[0]), new Integer(index), new Integer(maxlen + 1));
            results.add(new String(name).trim());
        }
        regCloseKey.invoke(root, new Integer(handles[0]));
        return results;
    }

    private static int[] createKey(Preferences root, int hkey, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return (int[])regCreateKeyEx.invoke(root, new Integer(hkey), WinRegistry.toCstr(key));
    }

    private static void writeStringValue(Preferences root, int hkey, String key, String valueName, String value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        int[] handles = (int[])regOpenKey.invoke(root, new Integer(hkey), WinRegistry.toCstr(key), new Integer(983103));
        regSetValueEx.invoke(root, new Integer(handles[0]), WinRegistry.toCstr(valueName), WinRegistry.toCstr(value));
        regCloseKey.invoke(root, new Integer(handles[0]));
    }

    private static byte[] toCstr(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); ++i) {
            result[i] = (byte)str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }

    static {
        try {
            regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", Integer.TYPE, byte[].class, Integer.TYPE);
            regOpenKey.setAccessible(true);
            regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", Integer.TYPE);
            regCloseKey.setAccessible(true);
            regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx", Integer.TYPE, byte[].class);
            regQueryValueEx.setAccessible(true);
            regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            regEnumValue.setAccessible(true);
            regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", Integer.TYPE);
            regQueryInfoKey.setAccessible(true);
            regEnumKeyEx = userClass.getDeclaredMethod("WindowsRegEnumKeyEx", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            regEnumKeyEx.setAccessible(true);
            regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", Integer.TYPE, byte[].class);
            regCreateKeyEx.setAccessible(true);
            regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", Integer.TYPE, byte[].class, byte[].class);
            regSetValueEx.setAccessible(true);
            regDeleteValue = userClass.getDeclaredMethod("WindowsRegDeleteValue", Integer.TYPE, byte[].class);
            regDeleteValue.setAccessible(true);
            regDeleteKey = userClass.getDeclaredMethod("WindowsRegDeleteKey", Integer.TYPE, byte[].class);
            regDeleteKey.setAccessible(true);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
