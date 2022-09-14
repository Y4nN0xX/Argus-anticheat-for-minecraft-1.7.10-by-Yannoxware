package fr.yannox.feurgus.utils.reflections;

public class GetClassFromName2 {
    public static Object fromName(Object n) throws Exception {
        return Class.forName((String)n);
    }
}
