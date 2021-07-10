
# Gradle for Android 

## 1.Gradle和Android Studio入门
Gradle中定义很多的任务，构建就是执行任务的动作，其中任务也会依赖于其他任务
- `gradle-wrapper.properties` 配置项目中使用的gradle版本封装，不依赖特定系统的gradle版本

## 2.基本自定义构建
### 2.1 基本项目构建文件架构
```
--build.gradle Project
    --build.gradle Module
--settings.gradle Project 
```
### 2.2 Task
- Gradle Android 插件 使用了-> Java基础插件 -> 基础插件 
- assemble 集合项目的输出
- clean 清理项目的输出
- check 运行所有检查，通常是单元测试和集成测试
- build 同时运行assemble和check
    ### Android 插件
    - assemble 构建版本apk (默认 assembleDebug、assembleRelease)
    - clean 删除所有构建内容
    - check 运行Lint检查，有问题时停止构建  (app/build/outputs/lint-result-release-fatal.html)
    - build 同时运行 assemble和check
    - installDebug和installRelease 设备/模拟器安装特定版本
    - uninstall
### 2.3 assemble输出版本中，自定义资源值和BuildConfig设置
```
buildTypes{
    resValue "String", "app_name", "Example Debug" //配置资源值，在<resource>中
    buildConfigField "String","API_URL","\"https://www.github.com\"" //在BuildConfig中
}
```
### 3.依赖管理
- 远程依赖
- 本地依赖
- 本地libs目录下，*.jar依赖
- 本地"com.android.library"依赖模块、打包成*aar，进行依赖
- 依赖库命名：group:name:version 
    - version：major.minor.patch   major不兼容API变化，minor向后兼容方式添加功能，patch修复Bug
- 编译拉取最新依赖库：group:name:version.+
- 依赖关键字：
    - apk 依赖库只打包到apk，不保存在本地
    - provided 依赖库只保存在本地，不打包到apk
    - implementation 依赖库打包到apk，并保存在本地
    - testImplementation、DebugImplementation 打包到特定类型apk
### 4.创建构建variant
- variant 由`buildType`和`productFlavors`共同决定
    - buildType 构建类型
    - productFlavors 
- 不同类型的覆盖规则
    - 源集：variant不同类型有一个存放资源和代码的目录 默认有个main源集 
    - 1.代码方面：variant和main中存放的源代码位置只能选其一
    - 2.对于资源和manifest.xml文件按照优先级：buildTypes -> productFlavors -> main -> dependencies(依赖库)
- 忽略某个variant类型，那就不能使用gradlew、IDE中variant窗口没有这个variant类型
```groovy
        android.variantFilter { variant ->
            if (variant.buildType.name.equals('release')) {
                variant.getFlavors().each() { flavor ->
                    if (flavor.name.equals('blue')) {
                        variant.setIgnore(true);
                    }
                }
            }
        }
```
### 5.管理多模块构建
- 1.构建子目录下的多模块
```groovy
//依赖子目录library下的library1模块，PS: compile project(':library:library1')
root
--build.gradle
--settings.gradle
--app
    --build.gradle
--library
    --library1
        --build.gradle
    --library2
        --build.gradle
```
- 2.不同模块插件
    - 1.java模块：`apply plugin: 'java'`
    - 2.android模块:包括代码和资源文件 `apply plugin: 'com.android.library'`
- 3.依赖Android Wear模块：`wearApp project('wear')`
- 4.执行模块任务时：
    - 1.如果多个模块都有相同任务，都会处理。PS：如果有个app和app2模块 `gradlew assemableDebug`，不同模块会生成不同的Debug包
    - 2.使用`gradlew :app2:assembleDebug`只会生成app2模块的Debug包
- 5.加速构建
    - 1.`gradle.properties`中配置parallel属性`org.gradle.parallel=true`会根据CPU核心数并行执行构建模块。
    - 2.如果模块之间有互相访问对方的任务或属性，就是模块之间耦合，那并行构建会失效。
- 6.闭包(Cloure)：匿名的方法块，可以传递参数、返回值，可以当作变量使用。
    ```
        Closure c = {...};
        c.delegate = ..;//设置代理类
        c.call();//调用
        c.setResolveStrategy(Closure.OWNER_FIRST);//设置代理策略
    ```
    - this `定义该闭包所在的类`
    - owner `定义该闭包所在的类或闭包`，就是说如果闭包定义在类中那么this==owner
    - delegate `和owner一样，或者自己设置delegate类` 闭包可以访问代理类(delegate)的方法和变量
    - 闭包中对于同名调用的处理策略（比如：代理类和闭包定义的类中存在相同的方法）
        - Closure.OWNER_FIRST 默认策略，优先在owner中找，再去delegate中找
        - Closure.DELEGATE_FIRST 优先在delegate中找，再去owner中找
        - Closure.OWNER_ONLY 只在owner中找
        - Closure.DELEGATE_ONLY 只在delegate中找
        - Closure.TO_SELF 自定义部分
```groovy
dependencies{ //Project#dependencies(Closure configureClosure)
            // 这闭包通过DependencyHandler的代理对象，就是说闭包是调用DependencyHandler中的#add()方法，
            // 里面应该也有一些对add调用省略处理这些七七八八。
    implementation 'xxx'
    implementation "xxx"
}
```    
 ### 7.创建任务和插件 *
 - [!Groovy官网](http://groovy-lang.org/download.html)
 - 语法
   - 1.类、方法默认 共有`public`
   - 2.方法调用，有参数情况下省略`()`
   - 3.方法调用，省略`;`
   - 4.变量定义`def`
   - 5.字符串处理：单引号`''`和双引号`""`，单引号只能是字符串常量，双引号中可以使用`$`来调用`表达式`和`变量`
   - 6.闭包(Cloure)：匿名的方法块，可以传递参数、返回值，可以当作变量使用。
```groovy
dependencies{ //Project#dependencies(Closure configureClosure)
            // 这闭包通过DependencyHandler的代理对象，就是说闭包是调用DependencyHandler中的#add()方法，
            // 里面应该也有一些对add调用省略处理这些七七八八。
    implementation 'xxx'
    implementation "xxx"
}
```
   - 7.Gradle构建的过程
        - 1.从Task初始化->配置->执行过程
        - 2. doFirst、doLast执行阶段，其中doFirst添加越靠前执行，doLast添加越靠后执行
   - 8.Hook Android插件
        - 1. #TheTask.dependOn "xxxTask" :表示在`TheTask`之前执行`xxxTask` 
            (利用自定义task，把签名密钥不提交到git上，任务执行时，加入密钥)
        - 2. `applicationVariants`一些构建处理：比如改生成的包名等
        - 3. 需要实现Plugin接口创建插件
```groovy
task hello {
    println "hello"
}
task hello  {
    //<< Could not find method leftShift() for arguments  << 在Gradle 4.x中被弃用，Gradle 5.0被移除
    println 'Hello'
    doFirst {
        println 'Hello'
    }
    doFirst {
        println 'Hello2'
    }
    doLast {
        println 'Last'
    }
    doLast {
        println 'Last2'
    }
}
/**输出
> Task :app:hello
Hello2
Hello
Last
Last2
**/
```
 ### 8.持续集成(CI Continuous Integration )
- 自动化构建验证：一些CI软件：Jenkins、TeamCity、Travis CI
### 9.高级自定义构建
 - 1.减少包大小
    - `shrinkResources = true`时，需要保留某些资源文件（PS:反射动态使用资源）需要保留，在`res/raw`下，叫做`keep.xml`定义保留的资源。
    - `defaultConfig { resConfigs "en","xxhdpi" }`：配置只想保留的资源，其它会被移除。
 - 2.加快构建
    - `org.gradle.parallel=true` `org.gradle.daemon=true`
    - `JVM参数设置 org.gradle.jvmargs=-Xms256m -Xms1024m`
    - `org.gradle.configureondemand`：忽略正在执行的task不需要的模块
    - `--profile 显示构建耗时`
