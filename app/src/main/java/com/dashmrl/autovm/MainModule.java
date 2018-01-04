package com.dashmrl.autovm;

import android.arch.lifecycle.ViewModelProviders;

import com.dashmrl.autovm.repo.BaseRepo;

import dagger.Module;
import dagger.Provides;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         7:00 AM
 * Email        xinliugm@gmail.com
 */
@Module
public class MainModule {
    private MainActivity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    public MainContract.BaseMainPresenter provideMainPresenter(Holder_MainPresenter_AutoVM factory) {
        return ViewModelProviders.of(activity, factory).get(factory.getType());
    }

    @Provides
    public BaseRepo provideRepo() {
        return new Repo();
    }
}
