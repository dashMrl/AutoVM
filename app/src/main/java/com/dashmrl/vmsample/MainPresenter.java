package com.dashmrl.vmsample;

import com.dashmrl.vm.VM;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:57 AM
 * Email        xinliugm@gmail.com
 */

public class MainPresenter extends MainContract.BaseMainPresenter {
    private String msg;

    @VM
    public MainPresenter(String msg) {
        this.msg = msg;
    }

    @Override
    public void loadMsg() {
        MainContract.MainView view = retrieveView();
        if (view != null) {
            view.onLoad(msg);
        }
    }
}
