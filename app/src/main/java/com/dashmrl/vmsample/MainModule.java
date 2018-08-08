package com.dashmrl.vmsample;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import com.dashmrl.vm.VMFactory;
import com.dashmrl.vmsample.repo.BaseRepo;
import com.dashmrl.vmsample.repo.Repo;

import javax.inject.Named;

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
    public MainContract.BaseMainPresenter provideMainPresenter( @Named("local") BaseRepo local,@Named("remote") BaseRepo remote) {
        ViewModelProvider.NewInstanceFactory factory = VMFactory.withvm(MainPresenter.class, local, remote);
        return ViewModelProviders.of(activity, factory).get(MainPresenter.class);
    }

    @Provides
    @Named("remote")
    public BaseRepo provideRemoteRepo() {
        return new Repo();
    }

    @Provides
    @Named("local")
    public BaseRepo provideLocalRepo() {
        return new Repo();
    }
}
