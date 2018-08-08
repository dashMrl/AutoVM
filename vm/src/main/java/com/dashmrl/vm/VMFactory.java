package com.dashmrl.vm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Author  xavier
 * Date    2018/8/8
 */
public class VMFactory {

    public static <T extends ViewModel, R extends ViewModelProvider.NewInstanceFactory> R withvm(Class<T> vmClazz, Object... params) {
        try {
            Class<?> vmFactoryClazz = Class.forName(vmClazz.getCanonicalName() + "_VMFACTORY");
            Class<?>[] parameterTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                parameterTypes[i] = params[i].getClass();
            }
            for (Constructor<?> declaredConstructor : vmFactoryClazz.getDeclaredConstructors()) {
                Class<?>[] constructorParameterTypes = declaredConstructor.getParameterTypes();
                if (areParameterTypesMatch(constructorParameterTypes, parameterTypes)) {
                    return (R) declaredConstructor.newInstance(params);
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            try {
                throw e;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private static boolean areParameterTypesMatch(Class<?>[] constructorParameterTypes, Class<?>[] parameterTypes) {
        if (constructorParameterTypes.length == parameterTypes.length) {
            for (int i = 0; i < constructorParameterTypes.length; i++) {
                if (!constructorParameterTypes[i].isAssignableFrom(parameterTypes[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
