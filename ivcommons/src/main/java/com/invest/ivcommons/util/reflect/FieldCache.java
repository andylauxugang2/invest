package com.invest.ivcommons.util.reflect;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类字段缓存
 * Created by xugang on 16/12/25.
 */
public class FieldCache {

    private static ConcurrentHashMap<Class<?>, Map<String, Field>> fieldsMap = new ConcurrentHashMap<Class<?>, Map<String, Field>>();

    /**
     * @param clazz 类名
     * @param field 字段名
     * @return 返回类的指定 Field 对象，已经设置 Accessible
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static Field getField(Class<?> clazz, String field) {
        return ensureFieldsStored(clazz).get(field);
    }

    /**
     * @param clazz
     * @return 目标类全部字段组成的信息
     */
    public static Collection<Field> getFields(Class<?> clazz) {

        return ensureFieldsStored(clazz).values();
    }

    /**
     * 确认缓存中已存在目标类字段信息并返回该字段信息
     *
     * @param clazz
     * @return 目标类字段信息
     */
    private static Map<String, Field> ensureFieldsStored(Class<?> clazz) {

        Map<String, Field> result = fieldsMap.get(clazz);
        if (result == null) {
            Field[] fields = clazz.getDeclaredFields();
            //按字段名自然顺序排序，确保每次返回顺序相同
            Arrays.sort(fields, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            result = new LinkedHashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                result.put(field.getName(), field);
            }
            fieldsMap.putIfAbsent(clazz, result);
        }
        return result;
    }

    /**
     * 清除目标类的缓存信息
     *
     * @param clazz
     */
    public static void remove(Class<?> clazz) {
        fieldsMap.remove(clazz);
    }

}
