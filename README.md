
# Dagger2
- 1.初始化
```groovy
apply plugin: 'kotlin-kapt'

dependencies {
    implementation 'com.google.dagger:dagger:2.x'
    kapt 'com.google.dagger:dagger-compiler:2.x'
}
```
- 1.获取实例
    - `@Binds`通知Dagger接口，采用何种实现
    - 1.通过`@Inject`进行构造函数注入
    - 2.使用带有`@Module`注释的类，其中`@Providers`通知Dagger提供项目中不具备的类
    
- 2.`@Component`告知某个类需要依赖注入,`方法声明`传入需要的类对象参数 PS：使用Module提供类实例时需要`@Component(modules = [UserModule::class])`


- 3.作用域注解：
    - `@Singleton`：Dagger中唯一的作用域注解，标识获取应用中的唯一实例 `为整个应用中重复使用的对象添加注释`
    - 自定义作用域注解：`@ApplicationScope`、`@LoggedUserScope`、`@ActivityScope`
        - 1.这个`@Component`在哪个类初始化/依赖注入，就标识包含依赖对象的作用域范围
        - 2.需要在`@Component`中，设置该`@MyCustomScope`注解，同时在依赖对象的类中设置该`@MyCustomScope`注解
```kotlin
  @Scope
  @MustBeDocumented
  @Retention(value = AnnotationRetention.RUNTIME)
  annotation class MyCustomScope //自定义注解名称
```
- 4.子组件`@Subcomponent`
```kotlin
@Subcomponent
interface LoginComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

    fun inject(loginActivity: LoginActivity)
}

@Module(subcomponents = LoginComponent::class)
class SubcomponentsModule {}

@Component(modules = [NetworkModule::class, SubcomponentsModule::class])
interface ApplicationComponent {
    fun loginComponent(): LoginComponent.Factory
}

class LoginActivity: Activity() {
    lateinit var loginComponent: LoginComponent //设置作用域为 LoginActivity 生命周期的 LoginComponent 对象

    // Fields that need to be injected by the login graph
    @Inject lateinit var loginViewModel: LoginViewModel //LoginViewModel作用域只在 LoginActivity 中

    override fun onCreate(savedInstanceState: Bundle?) {
        // Creation of the login graph using the application graph
        loginComponent = (applicationContext as MyDaggerApplication)
                            .appComponent.loginComponent().create()
        // Make Dagger instantiate @Inject fields in LoginActivity
        loginComponent.inject(this)
        // Now loginViewModel is available
        super.onCreate(savedInstanceState)
    }
}
```
