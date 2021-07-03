
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
