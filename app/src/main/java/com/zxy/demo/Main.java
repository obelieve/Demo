package com.zxy.demo;

import android.sax.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import kotlin.jvm.JvmField;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
                System.out.println(method.getGenericParameterTypes());//方法形式参数 类型
                System.out.println(method.getParameterAnnotations());//方法形式参数注解
                System.out.println(method.getGenericReturnType());//方法返回类型
                return method.invoke(new Fruit(),args);
            }
        });
        food.name("fruit",5);
        food.hashCode();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RUNTIME)
    public @interface MParams{
        String value() default "";
    }

    @Target(ElementType.PARAMETER)
    @Retention(RUNTIME)
    public @interface MM{
        String value() default "";
    }

    public interface Food{
        String name(@MParams @MM String content,@MM int num);
    }

    public static class Fruit implements Food {

        @Override
        public String name(String content, int num) {
            String name = "Fruit";
            System.out.println(name);
            return name;
        }
    }
}
