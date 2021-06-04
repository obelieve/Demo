package com.zxy.demo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class ReflectDemo {

    private String name;

    public static void main(String[] args) {
        Class<ReflectDemo> clazz= ReflectDemo.class;
        String tag = "";
        tag+=clazz.getSimpleName()+"\n";
        tag+=clazz.getCanonicalName()+"\n";
        tag+=clazz.getName()+"\n";

        System.out.println(tag);
        MethodProcessor processor = new MethodProcessor();
        ServiceInterface service = (ServiceInterface)Proxy.newProxyInstance(ServiceInterface.class.getClassLoader(), new Class[]{ServiceInterface.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.getDeclaringClass()==Object.class){
                    return method.invoke(this,args);
                }
                return processor.invoke(method,args);
            }
        });
        try {
            service.get("title","name",10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class MethodProcessor<T>{
        public T invoke(Method method,Object[] args){
            StringBuilder print=new StringBuilder();
            print.append("Method");
            print.append("\n方法名:"+method.getName());
            print.append("\n方法注解:");
            for(int i=0;i<method.getAnnotations().length;i++){
                print.append(getAnnotationName(method.getAnnotations()[i])+",");
            }
            print.append("\n方法参数:");
            for(int i=0;i<args.length;i++){
                print.append(""+args[i]+",");
            }
            print.append("\n方法参数注解:");
            for(int i=0;i<method.getParameterAnnotations().length;i++){
                print.append("\n"+"i:"+i+" ");
                for(int j=0;j<method.getParameterAnnotations()[i].length;j++){
                    print.append(getAnnotationName(method.getParameterAnnotations()[i][j])+",");
                }
            }
            print.append("\n方法参数类型:");
            //method.getTypeParameters() 方法泛型参数
            //method.getParameterTypes() 方法形式参数类型 返回class[]
            for(int i=0;i<method.getGenericParameterTypes().length;i++){
                print.append("\n"+"i:"+i+" ");
                print.append(method.getGenericParameterTypes()[i]+",");
            }
            System.out.println(print.toString());
            return (T) print.toString();
        }

//        private static String parseAnnotation(Annotation ){
//
//        }

        private static String getAnnotationName(Annotation annotation){
            if(annotation instanceof MethodAno){
                return "MethodAno"+getAnnotationValue(((MethodAno) annotation).value());
            }else if(annotation instanceof ParameterAno){
                return "ParameterAno"+getAnnotationValue(((ParameterAno) annotation).value());
            }else{
                return "";
            }
        }

        private static String getAnnotationValue(String value){
            if(value!=null&&value.length()>0){
                return " value="+value;
            }else{
                return "";
            }
        }
    }

    /**接口方法**/
    public interface ServiceInterface{
        @MethodAno
        <T,W> String get(String title,@ParameterAno("name") String name,@ParameterAno int num) throws Exception,ExceptionInInitializerError;
    }
    /********/

    /***注解定义***/
    @Target(PARAMETER)
    @Retention(RUNTIME)
    public @interface ParameterAno{
        String value() default "";
    }

    @Target(METHOD)
    @Retention(RUNTIME)
    public @interface MethodAno{
        String value() default "";
    }
    /**********/

    /**
     Java Type接口：https://blog.csdn.net/baidu_19338587/article/details/103276196
     ParameterizedType：参数化类型，带有泛型的类型 PS:List<String>
     TypeVariable：类型变量，就是泛型
     Class:
     **/
}
