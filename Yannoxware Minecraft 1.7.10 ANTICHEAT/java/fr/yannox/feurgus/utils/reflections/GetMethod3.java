package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.GetMethod2;

public class GetMethod3 {
    public static Object getMethod(Object a, Object b) throws Exception {
        return GetMethod2.getMethod(Class.forName((String)a), b);
    }
}
