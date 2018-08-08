package com.dashmrl.vmsample;

import com.dashmrl.vm.VM;
import com.dashmrl.vmsample.repo.BaseRepo;

import javax.inject.Named;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:57 AM
 * Email        xinliugm@gmail.com
 */

public class MainPresenter extends MainContract.BaseMainPresenter {
    private BaseRepo repo;

    @VM( withType = true)
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
