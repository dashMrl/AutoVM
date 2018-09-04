package com.dashmrl.vmsample.mvp;

import android.app.Fragment;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;

import java.lang.ref.WeakReference;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:49 AM
 * Email        xinliugm@gmail.com
 */

public abstract class BasePresenter<V> extends ViewModel implements LifecycleObserver {

    private WeakReference<V> view = new WeakReference<V>(null);

    public void takeView(V v) {
        view.clear();
        view = new WeakReference<>(v);
        if (v instanceof LifecycleOwner) {
            takeLifecycle(((LifecycleOwner) v));
        }
    }


    public void takeLifecycle(LifecycleOwner life) {
        life.getLifecycle().addObserver(this);
    }

    protected final V retrieveView() {
        V v = view.get();
        if (v == null) {
            return null;
        }
        if (v instanceof Fragment && ((Fragment) v).isDetached() ||
                v instanceof android.app.Fragment && ((Fragment) v).isDetached()) {
            dropView();
            return null;
        }
        return v;
    }

    public void dropView() {
        view.clear();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void

    onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void

    onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void

    onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void

    onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void

    onStop() {
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        dropView();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
