# AutoVM
[![](https://jitpack.io/v/dashMrl/AutoVM.svg)](https://jitpack.io/#dashMrl/AutoVM)

code generator for ViewModel of Android Architecture Components.

AutoVM help us with creating Factory for each ViewModel's subclass (non-abstract and non-private)


## Convenient Api
You can create an instance of your ViewModel with only one sentence:
```java
MainViewModel mvm = VMFactory.viewmodel(fragAct, MainViewModel.class, param0, param1);
```
This api is implemented with dynamic proxy, it will spend more generator Proxy class at runtime and
finding a constructor to create an `ViewModel` instance for the first time, it will cache the
constructors for incoming calls.

> you should not provider a private constructor with zero argument

## Generator Code with Annotation Processor
If you can stand with lower performance with reflection/dynamic proxy, you can :
1. Create your own ViewModel,like this:
```java
public class MainVM extends ViewModel{
    @VM
    public MainViewModel(@NonNull BaseRepo repo){
//      ...        
    }
}
```
2. Press `Ctrl/Cmd` + `F9` ,you will get `MainVM_FACTORY` :
```java
public class MainVM_FACTORY extends ViewModelProvider.NewInstanceFactory {
  private BaseRepo repo;

  public MainVM_FACTORY(@NonNull BaseRepo repo) {
    this.repo = repo;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if(modelClass.isAssignableFrom(com.dashmrl.autovm.MainVM.class)) {
      try {
        return modelClass.getConstructor(com.dashmrl.autovm.repo.BaseRepo.class).newInstance(repo);
      } catch(Exception e) {
          e.printStackTrace();
        }
      }
      return super.create(modelClass);
    }

  }
```

3. Use it:
```java
MainVM_FACTORY f = new MainVM_FACTORY(repo);
MainVM vm = ViewModelProviders.of(this,f).get(f.getType());
```
Then enjoy it!!

## How to integrate
1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency to your app module:
If you'd like the dynamic proxy way:
```groovy
dependencies {
    implementation 'com.github.dashMrl.AutoVM:vm:v0.0.3'
}
```
or annotation processor:
```groovy
dependencies {
    implementation 'com.github.dashMrl.AutoVM:vm-annotation:v0.0.3'
    annotationProcessor 'com.github.dashMrl.AutoVM:vm-processor:v0.0.3'
}
```

## Note
Attentions:
- your ViewModel should not be modified by abstract or private
- only **one** constructor can be annotated
- if Dagger2 is not implemented in your project,set `injectable` to `false`

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