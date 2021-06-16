package com.zxy.demo;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            List<Integer[]> lists = new ArrayList<>();
            service.get("title","name",10,new ArrayList<>(),lists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        A.info();
    }

    static class A<T>{
        T list;
        List<? extends A<String>> list2;
        List<? extends A<String>> list3;
        List<A<T>>[] list4;

        static void info(){
            try {
                System.out.println("普通类型");
                Field field = A.class.getDeclaredField("list");
                System.out.println(field.getGenericType() instanceof Class<?>);
                System.out.println(field.getGenericType() instanceof TypeVariable);
                A.printTypeInfo("field1", field);
                System.out.println("泛型类型");
                Field field2 = A.class.getDeclaredField("list2");
                Type type2 = ((ParameterizedType) field2.getGenericType()).getActualTypeArguments()[0];
                A.printTypeInfo("field2", field2);
                System.out.println("泛型类型<? extends A<String>>");
                System.out.println((type2 instanceof WildcardType) + " WildcardType");
                System.out.println(((WildcardType) type2).getLowerBounds().length + " " + ((WildcardType) type2).getUpperBounds().length);
                System.out.println((((WildcardType) type2).getUpperBounds()[0]) instanceof ParameterizedType);
                Field field3 = A.class.getDeclaredField("list3");
                System.out.println("list2 getRawType(list3)" + (A.getRawType(field2.getGenericType()) == A.getRawType(field3.getGenericType())));
                System.out.println("list2 equals list3" + (field2.getGenericType().toString().equals(field3.getGenericType().toString())));
                System.out.println("数组类型");
                Field field4 = A.class.getDeclaredField("list4");
                A.printTypeInfo("field4", field4);
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        static void printTypeInfo(String tag, Field field){
            System.out.println(tag+" Type ="+field.getType());
            System.out.println(tag+" GenericType ="+field.getGenericType());
            System.out.println(tag+" getRawType ="+A.getRawType(field.getGenericType()));
        }

        /**
         * Retrofit 用来判断返回类型的
         * @param type
         * @return
         */
        public static Class<?> getRawType(Type type) {
            Objects.requireNonNull(type, "type == null");

            if (type instanceof Class<?>) {
                // Type is a normal class.
                return (Class<?>) type;
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;

                // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
                // suspects some pathological case related to nested classes exists.
                // ZXYNOTE: 2021/6/7 22:42 *****v(-2.1.1)***** ParameterizedType.getRawType()是返回最外层的类型
                Type rawType = parameterizedType.getRawType();
                if (!(rawType instanceof Class)) throw new IllegalArgumentException();
                return (Class<?>) rawType;
            }
            // ZXYNOTE: 2021/6/7 22:42 *****v(-2.1.2)***** type判断是否是GenericArrayType 泛型数组 PS: T[]
            if (type instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) type).getGenericComponentType(); //获取泛型数组的组件类型
                return Array.newInstance(getRawType(componentType), 0).getClass(); // ZXYNOTE: 2021/6/8 17:07  *****??***** 泛型数组类型判断需要递归？
            }
            if (type instanceof TypeVariable) {
                // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
                // type that's more general than necessary is okay.
                return Object.class;
            }
            if (type instanceof WildcardType) {
                // ZXYNOTE: 2021/6/8 17:12 *****??***** 通配符类型判断需要递归？ PS:List<? extend Fruit> extends是上界只能get, List<? super Apple> super是下界，只能add （编译器判断list是Apple父类组成）
                //? extend Fruit 和 Apple 之前用这个只是类继承时使用到
                return getRawType(((WildcardType) type).getUpperBounds()[0]);
            }

            throw new IllegalArgumentException(
                    "Expected a Class, ParameterizedType, or "
                            + "GenericArrayType, but <"
                            + type
                            + "> is of type "
                            + type.getClass().getName());
        }
    }

    static class Food{

    }

    static class Fruit extends Food{

    }


//    A<String> a1=new A<>("aaa");
//    a1.t();
//    List<? extends Fruit> list = new ArrayList<Apple>(){
//        {
//            add(new Apple());
//            add(new Apple());
//        }
//    };
//
//    List<? super Apple> list2 = new ArrayList<>();
//        list2.add(new Apple());
//    System.out.println(list2.get(0).getClass().getSimpleName());
//    System.out.println(list.get(0).getClass().getSimpleName());

//    public static class Fruit{
//        public void ff(){
//
//        }
//    }
//
//    public static class Apple extends Fruit{
//        public void aa(){
//
//        }
//    }


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
                if(method.getGenericParameterTypes()[i] instanceof ParameterizedType){
                    print.append("泛型 "+((ParameterizedType) method.getGenericParameterTypes()[i]).getRawType()+",");
                }else{
                    print.append(method.getGenericParameterTypes()[i]+",");
                }
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
        <T,W> String get(String title, @ParameterAno("name") String name, @ParameterAno int num,
                         @ParameterAno List<String> stringList,
                         @ParameterAno List<Integer[]> lists) throws Exception,ExceptionInInitializerError;
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
