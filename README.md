# AutoVM
code generator for ViewModel of Android Architecture Components.

AutoVM help us with creating Factory for each ViewModel's subclass (non-abstract and non-private)



## How to use
1. Create your own ViewModel,like this:
```java
public class MainViewModel extends ViewModel{
    @AutoVM(injectable=true,withType=true)
    public MainViewModel(BaseRepo repo){
//      ...        
    }
}
```
2. Press `Ctrl` + `F9` ,you will get `MainViewModel_AutoVM` :
```java
public class MainVM_AutoVM extends ViewModelProvider.NewInstanceFactory {
  private BaseRepo repo;

  @Inject
  public MainVM_AutoVM(BaseRepo repo) {
    this.repo = repo;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if(modelClass.isAssignableFrom(com.dashmrl.autovm.MainVM.class)) {
      try {
        return modelClass.getConstructor(com.dashmrl.autovm.repo.BaseRepo.class).newInstance(repo);
      } catch(Exception e) {
          e.printStackTrace();
        }
      }
      return super.create(modelClass);
    }

    public Class<MainVM> getType() {
      return com.dashmrl.autovm.MainVM.class;
    }
  }
```

As you can see,we got a custom subclass of  ViewModelProvider.NewInstanceFactory.
What's more? The Constructor is annotated with `@Inject` and `getType()` method which returns the MainVM.class
If you don't need them,just set `injectable` or `withType` to `false`.

3. Use it:
```java
MainVM_AutoVM f = new MainVM_AutoVM(repo);
MainVM vm = ViewModelProviders.of(this,f).get(f.getType());
```
Then enjoy it!!

## How to integrate
1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency



## Note
Need to pay attention to these:
- your ViewModel should not be modified by abstract or private
- if Dagger2 is not implemented in your project,set `injectable` to `false`


## License
```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/
   Copyright 2017 xiansenLiu

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