# AutoVM
[![](https://jitpack.io/v/dashMrl/AutoVM.svg)](https://jitpack.io/#dashMrl/AutoVM)

配合 Android jetpack `ViewModel` 使用的注解处理器，注解处理器会在编译期为 `ViewModel` 创建相应的
辅助类帮助创建它的实例。


## 使用

1. 创建 ViewModel 子类
```java
public class MainPresenter extends ViewModel{
    @VM
    public MainPresenter(String msg){
//      ...        
    }
}
```
2. 按 `Ctrl/Cmd` + `F9` 重新构建,得到 `MainPresenter$Creator`
```java
public class MainPresenter$Creator extends BaseCreator {
    public MainPresenter$Creator() {
        constructorParamTypess = new Class<?>[1][];
        constructorParamTypess[0] = new Class<?>[]{java.lang.String.class};
    }

    @Override
    public MainPresenter create(Object[] params) {
        int index = findMethodIndex(params);
        switch (index) {
            case 0: {
                return new MainPresenter(((java.lang.String) params[0]));
            }
        }
        return null;
    }
}
```

3. 使用
```java
MainPresenter presenter = VMCreator.viewModel(this, MainPresenter.class, "this is msg");
```
Then enjoy it!!

## 集成
1. 添加 JitPack 仓库到项目根目录的 build.gradle
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```
2. 给 app module 添加依赖
```groovy
dependencies {
    implementation 'com.github.dashMrl.AutoVM:vm:v0.0.4'
    implementation 'com.github.dashMrl.AutoVM:vm-annotation:v0.0.4'
    annotationProcessor 'com.github.dashMrl.AutoVM:vm-processor:v0.0.4'
}
```

## Proguard
```
-keepnames class * extends android.arch.lifecycle.ViewModel{
    <init>(...);
}
-keep class * extends com.dashmrl.vm.BaseCreator{
    <init>(...);
}
```

## 注意
- `VM` 注解只能用在非抽象类上
- `private` 修饰的构造会使用反射创建实例，`public` 将直接通过 `new` 关键字创建
- 基本类型会被当成包装类
- 构造器只有一个数组参数时，使用 **二维数组** 避免可变参数的一些问题


## TODOs
-[ ] 只有一个构造器时性能提升
-[ ] 去除 VMCreator 发射操作

## License
```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/
   Copyright 2018 dashMrl

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```