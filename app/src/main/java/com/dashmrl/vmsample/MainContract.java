package com.dashmrl.vmsample;

import com.dashmrl.vmsample.mvp.BasePresenter;
import com.dashmrl.vmsample.mvp.BaseView;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         6:56 AM
 * Email        xinliugm@gmail.com
 */

public interface MainContract {
    interface MainView extends BaseView {
        void onLoad(String msg);
    }

    abstract class BaseMainPresenter extends BasePresenter<MainView> {
        abstract public void loadMsg();
    }

}
