package net.seyarada.pandeloot.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldUtils {

    private static final Map<Class<?>, ConcurrentHashMap<String, Field>> fieldMap = new ConcurrentHashMap<>();

    public static void unlockField(Class<?> clazz, String field) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);

            ConcurrentHashMap<String, Field> map = fieldMap.get(clazz);
            if(map == null)
                map = new ConcurrentHashMap<>();
            map.put(field, f);
            fieldMap.put(clazz, map);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static Field getField(Class<?> clazz, String field) {
        if(!fieldMap.containsKey(clazz) || ! fieldMap.get(clazz).containsKey(field))
            unlockField(clazz, field);
        return fieldMap.get(clazz).get(field);
    }

}