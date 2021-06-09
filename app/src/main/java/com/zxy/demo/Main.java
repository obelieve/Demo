package com.zxy.demo;


import com.obelieve.frame.net.ApiBaseResponse;
import com.zxy.demo.entity.UserInfo;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;

import okhttp3.ResponseBody;

import okio.BufferedSink;

import okio.Okio;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class Main {

    public static void main(String[] args) throws Exception {
//        reqGet();
//        reqGetDownload();
//        reqPost();
//        Thread.sleep(500);
//        reqPost("11");
        System.out.println("普通类型");
        Field field = A.class.getDeclaredField("list");
        System.out.println(field.getGenericType() instanceof Class<?>);
        System.out.println(field.getGenericType() instanceof TypeVariable);
        A.printTypeInfo("field1",field);
        System.out.println("泛型类型");
        Field field2 = A.class.getDeclaredField("list2");
        Type type2 = ((ParameterizedType)field2.getGenericType()).getActualTypeArguments()[0];
        A.printTypeInfo("field2",field2);
        System.out.println("泛型类型<? extends A<String>>");
        System.out.println((type2 instanceof WildcardType)+" WildcardType");
        System.out.println(((WildcardType)type2).getLowerBounds().length+" "+((WildcardType)type2).getUpperBounds().length);
        System.out.println((((WildcardType)type2).getUpperBounds()[0]) instanceof ParameterizedType);
        Field field3 = A.class.getDeclaredField("list3");
        System.out.println("list2 getRawType(list3)"+(A.getRawType(field2.getGenericType())==A.getRawType(field3.getGenericType())));
        System.out.println("list2 equals list3"+(field2.getGenericType().toString().equals(field3.getGenericType().toString())));
        System.out.println("数组类型");
        Field field4 = A.class.getDeclaredField("list4");
        A.printTypeInfo("field4",field4);
    }

    static class A<T>{
        T list;
        List<? extends A<String>> list2;
        List<? extends A<String>> list3;
        List<A<T>>[] list4;


        static void printTypeInfo(String tag,Field field){
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

    /**
     * - 1.GET请求
     * 	- 1.带参数
     * 	- 2.字节流下载
     * - 2.POST请求
     * 	- 2.1 不带参数
     * 	- 2.2 带参数
     * 	- 2.3 多部分表格提交 multipart/form-data
     */

    private static ServiceInterface sServiceInterface = new Retrofit.Builder().baseUrl(ServiceInterface.Companion.getBASE_URL()).client(
            new OkHttpClient.Builder().addInterceptor(new HttpInterceptor()).build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ApiConverterFactory.Companion.create())
            .build().create(ServiceInterface.class);

    /**
     * Get请求
     */
    private static void reqGet() {
        sServiceInterface.getBaidu("https://www.baidu.com").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("reqGet百度 "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {

            }
        });
    }

    /**
     * Get请求下载，带有@Streaming
     */
    private static void reqGetDownload(){
        String url = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2103804686,792857297&fm=26&gp=0.jpg";
        sServiceInterface.downloadFile("Range: bytes=10-",url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    BufferedSink sink = Okio.buffer(Okio.sink(new File("C:\\Users\\Administrator\\Desktop", "testImage" + 1 + ".png")));
                    sink.writeAll(response.body().source());
                    sink.close();
                    System.out.println("req图片 ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {

            }
        });
    }

    /**
     * Post请求
     */
    private static void reqPost(){
        sServiceInterface.getUserInfo().subscribe(new Observer<ApiBaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<UserInfo> response) {
                System.out.println("获取用户信息"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("获取用户信息err "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Post请求带参数
     * @param nickname
     */
    private static void reqPost(String nickname){
        sServiceInterface.modifyUserInfo("nickname","",nickname,"").subscribe(new Observer<ApiBaseResponse<String>>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {

            }

            @Override
            public void onNext(@NotNull ApiBaseResponse<String> response) {
                System.out.println("修改用户信息:"+response.getData());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                System.out.println("修改用户信息err "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
