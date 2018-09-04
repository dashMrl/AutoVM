package com.dashmrl.vm;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         2:53 PM
 * Email        xinliugm@gmail.com
 */

public interface Constants {
    Clazz BaseCreator = new Clazz("com.dashmrl.vm", "BaseCreator");
    Clazz NonNull = new Clazz("android.support.annotation", "NonNull");
    Clazz Activity = new Clazz("android.support.v4.app", "FragmentActivity");
    Clazz Fragment = new Clazz("android.support.v4.app", "Fragment");
    Clazz ViewModel = new Clazz("android.arch.lifecycle", "ViewModel");
    Clazz ViewModelProvider = new Clazz("android.arch.lifecycle", "ViewModelProvider");
    Clazz ViewModelProvider_Factory = new Clazz("android.arch.lifecycle", "ViewModelProvider.Factory");
    Clazz ViewModelProviders = new Clazz("android.arch.lifecycle", "ViewModelProviders");

    class Clazz {
        public final String packageName;
        public final String className;

        public Clazz(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        @Override
        public String toString() {
            return packageName + "." + className;
        }
    }

}
