package com.dashmrl.vmsample;

import com.dashmrl.vm.VM;
import com.dashmrl.vmsample.repo.BaseRepo;

import java.util.List;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:57 AM
 * Email        xinliugm@gmail.com
 */

public class MainPresenter extends MainContract.BaseMainPresenter {

    @VM()
    public MainPresenter(List<Holder<BaseRepo>> repos,BaseRepo repo)  {
    }

    @Override
    public void loadMsg() {
        MainContract.MainView view = retrieveView();
        if (view != null) {
            view.onLoad("23333");
        }
    }

    public  static class Holder<T>{

    }
}
