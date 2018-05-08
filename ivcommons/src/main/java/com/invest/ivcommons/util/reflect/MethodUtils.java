package com.invest.ivcommons.util.reflect;

import java.lang.reflect.Field;

/**
 * Created by xugang on 16/12/27.
 */
public final class MethodUtils {
    //生成set方法名
    public static String generateSetMethodName(Field field) {
        String fieldName = field.getName();
        String camelName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return "set" + camelName;
    }

    //生成get方法名
    public static String generateGetMethodName(Field field) {
        String fieldName = field.getName();
        if (field.getType() == boolean.class) {
            if (fieldName.startsWith("is") && fieldName.length() > 2 && Character.isUpperCase(fieldName.charAt(2))) {
                return fieldName;
            }
        }
        String camelName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (field.getType() == boolean.class) {
            return "is" + camelName;
        } else {
            return "get" + camelName;
        }
    }
}
