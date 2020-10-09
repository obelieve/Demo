# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#######################################################################################
#@Author zxy
#注意事项：
#1.忽略warning信息
#如果确认app运行中和那些引用没有什么关系的话，就可以添加-dontwarn标签，就不会在提示这些warning信息了。
#如-dontwarn org.apache.**。
#2.使用@Keep注解来保留。如: @Keep、@KeepPublicGettersSetters
#3.使用-printconfiguration /mappings/full-r8-config.txt，可以打印项目使用到的所有Proguard文件（有些规则文件是编译时产生的）
#######################################################################################
#1.默认区
-optimizationpasses 5 # 设置混淆的压缩比率 0 ~ 7
-dontusemixedcaseclassnames # 混淆后类名都为小写
-dontskipnonpubliclibraryclasses # 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclassmembers # 指定不去忽略非公共的库的类的成员
-dontpreverify #不做预校验
-verbose #混淆错误时，打印错误堆栈信息
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆采用的算法. //!code/allocation/variable，
-keepattributes SourceFile,LineNumberTable #保留代码行号，方便异常信息的追踪
-keepattributes *Annotation* #保留注解
-keepattributes Signature ## 避免混淆泛型,这在JSON实体映射时非常重要，比如fastJson

#-dump class_files.txt #dump文件列出apk包内所有class的内部结构
#-printseeds seeds.txt #seeds.txt文件列出未混淆的类和成员
#-printusage unused.txt #usage.txt文件列出从apk中删除的代码
#-printmapping mapping.txt #mapping文件列出混淆前后的映射

#----默认保留区，避免混淆Android基本组件（加不加都可以，AndroidStudio aapt2工具编译时，对.xml中的内容有keep处理）
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**
#v4
-dontwarn android.support.v4.** #不提示V4包下错误警告
-keep class android.support.v4.**{*;} #保持下面的V4兼容包的类不被混淆
-keepclassmembers class * extends android.app.Activity{ # 保留Activity中的方法参数是view的方法，从而我们在layout里面编写onClick就不会影响
    public void *(android.view.View);
}

#避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
    native <methods>;
}
#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#避免混淆自定义控件类的get/set方法和构造函数
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * { #对于带有回调函数onXXEvent的，不能混淆
    void *(**On*Event);
}
-keep class **.R$* { #对R文件下的所有类及其方法，都不能被混淆
 *;
}
-keep class * implements android.os.Parcelable { #保留Parcelable序列化的类不能被混淆
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable { #保留Serializable 序列化的类不被混淆
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class * { #使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
        public <init>(org.json.JSONObject);
}
#######################################################################################

#######################################################################################
#2.使用到的第三方包

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#okhttp3
-keep class com.squareup.okhttp.** { *;}
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

#gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#######################################################################################

#######################################################################################
#3.自己项目中的类
#使用webview-------------------------------------------------------------------
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}

#与js互相调用的类
-keepclassmembers class com.xx.Activity$JsInteraction {
  public *;
}
#----------------------------------------------------------------------------
#bean文件
#-keep class com.xxx.xxx.entity.** {*;}
-keep class * implements com.zxy.frame.utils.proguard.UnProguard {*;}
#反射类


