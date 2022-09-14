package fr.yannox.feurgus.utils.reflections;

public class GetMethod2 {
    public static Object getMethod(Object a, Object b) throws Exception {
        return ((Class)a).getDeclaredMethod((String)b, new Class[0]);
    }
}
