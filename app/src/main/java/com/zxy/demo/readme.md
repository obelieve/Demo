Android Data Binding Library 使用

1.配置

module中的build.gradle文件中，

dataBinding{
enabled true
}
-------------------------
XML
2.1 xml文件中表示：< 需要转义为&lt;
<layout>
  xmlns:bind="http://schemas.android.com/apk/res-auto"
    <data class ="com.zxy.demo.Binding"> 可以通过自己定义class 的值，指定绑定类的名称
        <import type="com.zxy.demo.User" alias="USER"/> 也可以使用别名USER来标识type
        <import type="com.zxy.demo.User"/>
        <variable name="user" type="User"/>
        <variable name="ok" type="java.util.ArrayList&lt;String>"/>
    </data>
    <view>原先的xml布局视图
    <include layout="@layout/name" bind:user="@{user}"> 支持include 传递user对象值
    </view>
</layout>

2.2 在布局中语法(syntax)：   @{}，也可以支持静态方法和域的表达
｛
tips:标签
<plurals></plurals> 表示复数形式
PS:
<plurals name="buy_kindle">
<item quantity="one">I want to buy a Kindle</item>
<item quantity="other">I want to buy some Kindles</item>
</plurals>
<xliff:g></xliff:g> 表示数据就是原本的内容
｝

2.3 编译时会产生新的类，规则：xml文件名+Binding   PS：activity_main.xml -> 生成 ActivityMainBinding.java文件

2.4 activity 中使用：
DataBindingUtil.setContentView(activity,layout_id);
在Fragment,ListView ,RecycleView中，
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
// or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);

2.5 表达式不会出现空的异常，如果未赋值的话；

2.6 事件处理

2.6.1 Method reference:方法的参数和监听器实际的参数要相同

android:onClick = "方法" 例如：onSaveClick(View view);
android:onClick="@{presenter::onSaveClick}"(与onClick原生的方法不同是，它是编译时生成，编译错误有提示)

2.6.2 Listener bindings:方法返回的值和监听器实际返回值相同就可以

在事件发生时，绑定表达式，
android:onClick="@{(view)->presenter.onSaveClick(view,user)}"/>
------------------------------
Observable data Objects

3.1
Observable:
8种基本类型,ObservableBoolean,ObservableByte等
ObservableField<T>
ObservableParcelable
集合,
对象, Observable 实现类 BaseObservable
例如：
ObservableField<String> firstName = new ObservableField<>();
BaseObservable{
get方法中添加 @Bindable
set方法中添加 notifyPropertyChanged(BR.firstName);
}
-------------
https://developer.android.google.cn/topic/libraries/data-binding/binding-adapters
Generated binding classes
Bind layout views to Architecture Components
-18-8-16- look less