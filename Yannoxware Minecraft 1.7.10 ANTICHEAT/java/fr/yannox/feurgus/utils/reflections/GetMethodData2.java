package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.GetMethod2;
import java.lang.reflect.Method;

public class GetMethodData2 {
    public static Object getMethod(Object b, Object c) throws Exception {
        Object p = GetMethod2.getMethod(b.getClass(), c);
        ((Method)p).setAccessible(true);
        return ((Method)p).invoke(b, new Object[0]);
    }

    public static Object getMethodWithArgs(Object a, Object b, Class<?> ... args) throws Exception {
        return ((Class)a).getDeclaredMethod((String)b, args);
    }

    public static Object getMethodFromClass(Object b, Object c, Class[] args) throws Exception {
        Object p = GetMethodData2.getMethodWithArgs(b, c, args);
        ((Method)p).setAccessible(true);
        return p;
    }
}
