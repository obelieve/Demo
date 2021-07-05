
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
