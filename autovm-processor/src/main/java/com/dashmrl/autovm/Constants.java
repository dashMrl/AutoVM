package com.dashmrl.autovm;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         2:53 PM
 * Email        xinliugm@gmail.com
 */

public interface Constants {
    Clazz ViewModel = new Clazz("android.arch.lifecycle", "ViewModel");
    Clazz Inject = new Clazz("javax.inject", "Inject");
    Clazz NonNull = new Clazz("android.support.annotation", "NonNull");
    Clazz ViewModelProvider_Factory = new Clazz("android.arch.lifecycle", "ViewModelProvider.NewInstanceFactory");

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
