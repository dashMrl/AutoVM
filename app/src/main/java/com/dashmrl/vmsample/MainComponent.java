package com.dashmrl.vmsample;

import dagger.Component;

/**
 * Author       xinliu
 * Date         1/4/18
 * Time         7:00 AM
 * Email        xinliugm@gmail.com
 */
@Component(modules = {MainModule.class})
public interface MainComponent {

    void inject(MainActivity activity);
}
