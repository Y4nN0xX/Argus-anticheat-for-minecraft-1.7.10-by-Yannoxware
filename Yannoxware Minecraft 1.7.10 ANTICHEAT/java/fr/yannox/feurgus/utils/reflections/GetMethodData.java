package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.GetMethod2;
import java.lang.reflect.Method;

public class GetMethodData {
    public static Object getMethod(Object a, Object b, Object c) throws Exception {
        Object p = GetMethod2.getMethod(Class.forName((String)a), c);
        ((Method)p).setAccessible(true);
        return ((Method)p).invoke(b, new Object[0]);
    }
}
