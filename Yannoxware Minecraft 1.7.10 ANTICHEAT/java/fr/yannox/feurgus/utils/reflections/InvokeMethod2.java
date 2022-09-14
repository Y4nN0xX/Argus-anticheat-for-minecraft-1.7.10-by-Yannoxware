package fr.yannox.feurgus.utils.reflections;

import java.lang.reflect.Method;

public class InvokeMethod2 {
    public static Object invk(Object meth, Object a, Object ... b) throws Exception {
        return ((Method)meth).invoke(a, b);
    }
}
