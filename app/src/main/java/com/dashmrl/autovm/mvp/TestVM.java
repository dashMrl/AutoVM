package com.dashmrl.autovm.mvp;

import android.support.annotation.NonNull;

import com.dashmrl.autovm.AutoVM;

/**
 * Author       xinliu
 * Date         1/5/18
 * Time         12:43 PM
 * Email        xinliugm@gmail.com
 */

public class TestVM extends BasePresenter<BaseView> {
    private String id;

    @AutoVM(injectable = true, withType = true)
    public TestVM(@NonNull String id) {
        this.id = id;
    }
}
