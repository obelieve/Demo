
# Lifecycle

### 使用
- 1.添加kotlin支持和依赖库
```gradle
#1.1 Project build.gradle
...
buildscript {
    ext.kotlin_version = '1.4.10'
    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        ...
    }
}
#1.2 app build.gradle
...
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'kotlin-android-extensions'

implementation "androidx.lifecycle:lifecycle-common-java8:2.2.0" //DefaultLifecycleObserver支持
implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"
kapt "androidx.lifecycle:lifecycle-compiler:2.2.0"
```
- 2.添加观察者监听Lifecycle
```kt
class LifecycleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(object: DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
            }         
        }
    }
}
```
- 3.自定义回调设置，LifecycleRegistry
```kt
class LifecycleActivity : AppCompatActivity() {

    var registery: LifecycleRegistry? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registery = LifecycleRegistry(this);
        registery!!.handleLifecycleEvent(Lifecycle.Event.ON_CREATE) //分发事件
        registery!!.addObserver(object: DefaultLifecycleObserver{   //监听处理
                    override fun onCreate(owner: LifecycleOwner) {
                    }
                });
    }
}
```

### 内部实现分析
- 1.Fragment/FragmentActivity 实现了#LifecycleOwner接口。通过getLifecycle()获取#Lifecycle对象。
- 2.内部实现了一个ReportFragment，来进行生命周期回调监听。
```java
public class ComponentActivity ...{
    public onCreate(Bundle savedInstanceState){
        ...
        ReportFragment.injectIfNeededIn(this);
        ...
    }
}

public class ReportFragment extends Fragment {
       @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            ...
            dispatch(Lifecycle.Event.ON_CREATE);
        }
    
    //最后调用
    static void dispatch(@NonNull Activity activity, @NonNull Lifecycle.Event event) {
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner) activity).getLifecycle().handleLifecycleEvent(event);
            return;
        }

        if (activity instanceof LifecycleOwner) {
            Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
            }
        }
    }

}
```



