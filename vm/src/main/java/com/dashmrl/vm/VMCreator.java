package com.dashmrl.vm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author xavier <a href="mailto:xinliugm@gmail.com">Contact me.</a>
 * @since 2018/8/20 00:38
 */
public class VMCreator {
    private static final String SUFFIX = "$Creator";
    private static Map<Class<? extends ViewModel>, BaseCreator> creatorMap = new WeakHashMap<>();

    @MainThread
    public static <T extends ViewModel> T viewModel(Fragment frag, Class<T> vmClazz, final Object... params) {
        return ViewModelProviders.of(frag, factory(params)).get(vmClazz);
    }

    @MainThread
    public static <T extends ViewModel> T viewModel(FragmentActivity activity, Class<T> vmClazz, final Object... params) {
        return ViewModelProviders.of(activity, factory(params)).get(vmClazz);
    }

    private static ViewModelProvider.Factory factory(final Object... params) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return getCreator(modelClass).create(params);
            }
        };
    }

    private static <T extends ViewModel> BaseCreator<T> getCreator(Class<T> vmClazz) {
        BaseCreator<T> baseCreator = creatorMap.get(vmClazz);
        if (baseCreator == null) {
            try {
                baseCreator = ((BaseCreator<T>) Class.forName(vmClazz.getCanonicalName() + SUFFIX).newInstance());
                creatorMap.put(vmClazz, baseCreator);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return baseCreator;
    }
}
