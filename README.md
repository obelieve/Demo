
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
    - `@ApplicationScope`
    - `@LoggedUserScope`
    - `@ActivityScope`