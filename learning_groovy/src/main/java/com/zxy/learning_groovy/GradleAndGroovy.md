# groovy用法

## 1.闭包(Closure)：一段匿名的方法体。 默认it参数。无参数时，it = null
1.1 闭包调用：1.closure.call() 2. closure()
1.2 闭包内方法调用顺序（存在方法查找顺序） this 当前类,owner 闭包,delegate 闭包的委托类
## 2. 默认public。（没有public/private/protected）
变量：
1. `def`定义。 ${}调用
方法：
1. print 'Hello World' 直接调用
2. def hello(){}
类：
1.get/set 自动

集合操作：
1. List list= [1,2,3,4,5]
2. Map map = ['a':1,'b':2]
