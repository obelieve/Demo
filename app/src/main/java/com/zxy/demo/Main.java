package com.zxy.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        //对对象方法的调用都会被重定向到一个调用处理器上。
        Food food = (Food)Proxy.newProxyInstance(Food.class.getClassLoader(), new Class[]{Food.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }
                boolean b=method.isDefault();
                System.out.println("动态代理:method="+method.getName()+" isDefault="+b);
                return method.invoke(new Fruit(),args);
            }
        });
        food.name();
        food.hashCode();
    }

    public interface Food{
        String name();
    }

    public static class Fruit implements Food {

        @Override
        public String name() {
            String name = "Fruit";
            System.out.println(name);
            return name;
        }
    }
}
