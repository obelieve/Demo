#第三方包的混淆保留规则汇总
#SmartRefreshLayout 没有使用到：序列化、反序列化、JNI、反射，所以并不需要添加混淆过滤代码，并且已经混淆测试通过
#picasso
-dontwarn com.squareup.okhttp.**

#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#jpinyin不需要


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

#newrelic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

#MediarGallery不需要
#android-gif-drawable

#leancloud
-keepattributes Signature
-dontwarn com.jcraft.jzlib.**
-keep class com.jcraft.jzlib.**  { *;}
-dontwarn sun.misc.**
-keep class sun.misc.** { *;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *; }
-dontwarn com.google.**
-keep class com.google.** { *;}
-dontwarn com.avos.**
-keep class com.avos.** { *;}
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-dontwarn android.support.**
-dontwarn org.apache.**
-keep class org.apache.** { *;}
-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.** { *;}
-dontwarn com.loopj.**
-keep class com.loopj.** { *;}
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**
-dontwarn org.xbill.**
-keep class org.xbill.** { *;}


-dontwarn com.baidu.**
-dontwarn com.tencent.**

#谷歌广告目前没用上
#-dontwarn com.google.ads.**
#-keep public class com.google.ads.**{
#    public protected *;
#}

#vlayout
-keepattributes InnerClasses
-keep class com.alibaba.android.vlayout.ExposeLinearLayoutManagerEx { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutParams { *; }
-keep class android.support.v7.widget.RecyclerView$ViewHolder { *; }
-keep class android.support.v7.widget.ChildHelper { *; }
-keep class android.support.v7.widget.ChildHelper$Bucket { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutManager { *; }

#腾讯IM
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**
-keep class tencent.**{*;}
-dontwarn tencent.**
-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

#华为推送
-keep class com.huawei.** {*;}
-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}
-keep class com.baidu.mapapi.**{*;}
#官方文档上的
#-ignorewarning #不能乱用
#-keepattributes *Annotation*
#-keepattributes Exceptions
#-keepattributes InnerClasses
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
#-keep class com.hianalytics.android.**{*;}
#-keep class com.huawei.updatesdk.**{*;}
#-keep class com.huawei.hms.**{*;}
#
#-keep class com.huawei.gamebox.plugin.gameservice.**{*;}
#
#-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
#-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
#-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}

#小米推送
#这里com.xiaomi.mipushdemo.DemoMessageRreceiver改成app中定义的完整类名
-keep class com.xiaomi.mipush.sdk.PushMessageReceiver {*;}
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**


#sharesdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**
-dontwarn **.R$*
