package com.invest.ivcommons.http.request;

import com.invest.ivcommons.http.ParamMapable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xugang on 17/1/4.
 */
public abstract class AbstractParamMap implements ParamMapable {

    private static Map<Class<?>, Class<?>> typeConverter = new HashMap<Class<?>, Class<?>>();

    static {
        typeConverter.put(Date.class, Date.class);
        typeConverter.put(List.class, List.class);
        typeConverter.put(ArrayList.class, List.class);
    }

    @Override
    public Map<String, Object> toParamMap() {
        Map<String, Object> map = new HashMap<>(16);
        try {
            Class clazz = this.getClass();
            Method[] methods = clazz.getMethods();

            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                String methodName = method.getName();
                if (!methodName.startsWith("get") || methodName.startsWith("getClass")) {
                    continue;
                }
                method.setAccessible(true);
                Object value = method.invoke(this);
                Class type = method.getReturnType();
                //field name
                String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

                if (typeConverter.get(type) == List.class) {
                    List list = (List) value;
                    if (list != null) {
                    } else {
                    }
                } else {
                    if (value != null) {
                        map.put(propertyName, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return map;
    }
}
