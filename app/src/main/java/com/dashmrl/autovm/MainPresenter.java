package com.dashmrl.autovm;

import com.dashmrl.autovm.repo.BaseRepo;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:57 AM
 * Email        xinliugm@gmail.com
 */

public class MainPresenter extends MainContract.BaseMainPresenter {
    private BaseRepo repo;

    @AutoVM(injectable = true, withType = true)
    public MainPresenter(BaseRepo repo, BaseRepo repo2) {
        this.repo = repo;
    }

    @Override
    public void loadMsg() {
        MainContract.MainView view = retrieveView();
        if (view != null) {
            view.onLoad("23333");
        }
    }
}
