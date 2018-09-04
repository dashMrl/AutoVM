package com.dashmrl.vm;

import android.arch.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/22 21:37
 */
public abstract class BaseCreator<T extends ViewModel> {
    protected Class<?>[][] constructorParamTypess;
    private Map<String, Integer> cacheMap = new WeakHashMap<>();

    public abstract T create(Object[] params);

    protected final int findMethodIndex(Object[] params) {
        if (params == null) {
            params = new Object[0];
        }
        StringBuilder keyBuilder = new StringBuilder(params[0].getClass().getCanonicalName());
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                paramTypes[i] = params[i].getClass();
                keyBuilder.append(paramTypes[i].getCanonicalName());
            } else {
                keyBuilder.append("null");
            }
        }
        String key = keyBuilder.toString();
        Integer index = cacheMap.get(key);
        if (index != null && index != -1) {
            return index;
        }
        for (index = 0; index < constructorParamTypess.length; index++) {
            Class[] constructorParamType = constructorParamTypess[index];
            if (areAssignableFrom(constructorParamType, paramTypes)) {
                cacheMap.put(key, index);
                return index;
            }
        }
        throw new RuntimeException("cannot find corresponding method for params: " + Arrays.asList(paramTypes).toString());
    }

    private static boolean areAssignableFrom(Class<?>[] constructorParameterTypes, Class<?>[] paramTypes) {
        if (constructorParameterTypes.length == paramTypes.length) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] != null && !constructorParameterTypes[i].isAssignableFrom(paramTypes[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
