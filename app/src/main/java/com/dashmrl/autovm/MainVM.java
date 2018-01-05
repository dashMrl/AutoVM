package com.dashmrl.autovm;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.dashmrl.autovm.repo.BaseRepo;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         7:19 PM
 * Email        xinliugm@gmail.com
 */

public class MainVM extends ViewModel {
    @AutoVM(injectable = true, withType = true)
    public MainVM(@NonNull BaseRepo repo) {
    }
}
