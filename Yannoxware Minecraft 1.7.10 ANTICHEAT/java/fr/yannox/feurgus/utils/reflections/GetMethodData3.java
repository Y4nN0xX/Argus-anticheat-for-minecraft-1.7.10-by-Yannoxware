package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.GetMethod2;
import java.lang.reflect.Method;

public class GetMethodData3 {
    public static Object getMethod(Object b, Object c) throws Exception {
        try {
            Object p = GetMethod2.getMethod(b.getClass(), c);
            ((Method)p).setAccessible(true);
            return ((Method)p).invoke(b, new Object[0]);
        }
        catch (Exception error) {
            Object p = GetMethod2.getMethod(b.getClass().getSuperclass(), c);
            ((Method)p).setAccessible(true);
            return ((Method)p).invoke(b, new Object[0]);
        }
    }
}
