package fr.yannox.feurgus.utils.reflections;

import fr.yannox.feurgus.utils.reflections.ClassExistsCheck2;

public class ClassExistsCheck {
    public static boolean exists(Object a) throws Exception {
        return ClassExistsCheck2.exists((String)a);
    }
}
