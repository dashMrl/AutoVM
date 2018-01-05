package com.dashmrl.autovm;

import com.dashmrl.autovm.repo.BaseRepo;

import javax.inject.Named;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:57 AM
 * Email        xinliugm@gmail.com
 */

public class MainPresenter extends MainContract.BaseMainPresenter {
    private BaseRepo repo;

    @AutoVM(injectable = true, withType = true)
    public MainPresenter(@Named("local") BaseRepo local,@Named("remote") BaseRepo remote) {
        this.repo = local;
    }

    @Override
    public void loadMsg() {
        MainContract.MainView view = retrieveView();
        if (view != null) {
            view.onLoad("23333");
        }
    }
}
