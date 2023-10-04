package fr.adrackiin.hydranium.api.utils;

import java.util.Arrays;

public class ReflectionUtils {

    public static boolean hasInterface(Class<?> c, Class<?> i){
        return Arrays.asList(c.getInterfaces()).contains(i);
    }

}
