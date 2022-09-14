package fr.yannox.feurgus.utils.reflections;

import java.lang.reflect.Field;

public class SetFieldAccessible2 {
    public static Object setFieldAccessible(Object a) throws Exception {
        ((Field)a).setAccessible(true);
        return a;
    }
}
