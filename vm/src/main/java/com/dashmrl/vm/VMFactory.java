package com.dashmrl.vm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author  xavier
 * Date    2018/8/8
 */
public class VMFactory {
    private static final Map<String, Constructor<?>> cacheMap = new WeakHashMap<>();

    public static <T extends ViewModel> T viewModel(Fragment frag, Class<T> vmClazz, final Object... params) {
        return ViewModelProviders.of(frag, factory(vmClazz, params)).get(vmClazz);
    }

    public static <T extends ViewModel> T viewModel(FragmentActivity activity, Class<T> vmClazz, final Object... params) {
        return ViewModelProviders.of(activity, factory(vmClazz, params)).get(vmClazz);
    }

    private static <T extends ViewModel> ViewModelProvider.Factory factory(Class<T> vmClazz, final Object... params) {
        if (params == null || params.length == 0) {
            return new ViewModelProvider.NewInstanceFactory();
        } else {
            return (ViewModelProvider.Factory) Proxy.newProxyInstance(vmClazz.getClassLoader(), new Class[]{ViewModelProvider.Factory.class}, (obj, method, objs) -> {
                if (method.getDeclaringClass() == Object.class
                        || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && method.isDefault()
                        || !method.getName().equals("create")) {
                    return method.invoke(obj, objs);
                }
                StringBuilder keyBuilder = new StringBuilder(objs[0].getClass().getCanonicalName());
                Class<?>[] paramTypes = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramTypes[i] = params[i].getClass();
                    keyBuilder.append(paramTypes[i].getCanonicalName());
                }
                final String key = keyBuilder.toString();
                Constructor<?> constructor = cacheMap.get(key);
                if (constructor != null) {
                    return constructor.newInstance(params);
                }
                for (Constructor<?> declaredConstructor: vmClazz.getDeclaredConstructors()) {
                    if (areAssignableFrom(declaredConstructor.getParameterTypes(), paramTypes)) {
                        cacheMap.put(key, declaredConstructor);
                        if (!declaredConstructor.isAccessible()) {
                            declaredConstructor.setAccessible(true);
                            Object instance = declaredConstructor.newInstance(params);
                            declaredConstructor.setAccessible(false);
                            return instance;
                        }
                        return declaredConstructor.newInstance(params);
                    }
                }
                cacheMap.remove(key);
                throw new RuntimeException("cannot create an instance of " + vmClazz.getCanonicalName() + " with params: " + Arrays.asList(paramTypes).toString());
            });
        }
    }

    private static boolean areAssignableFrom(Class<?>[] constructorParameterTypes, Class<?>[] paramTypes) {
        if (constructorParameterTypes.length == paramTypes.length) {
            for (int i = 0; i < paramTypes.length; i++) {
                if (!constructorParameterTypes[i].isAssignableFrom(paramTypes[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
