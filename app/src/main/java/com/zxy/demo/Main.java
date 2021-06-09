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
    static int[] a = new int[0];
    static List<String> list = new ArrayList<>();
    public static void main(String[] args) throws Exception {
//        reqGet();
//        reqGetDownload();
//        reqPost();
//        Thread.sleep(500);
//        reqPost("11");
        Field field = B.class.getDeclaredField("list");
        System.out.println("GenericType："+field.getGenericType()+" Type="+field.getType());
    }

    static class B<T>{
        T list;
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

    public static class A<T>{
        T[][] a;
        List<? extends String> b;
        String c;
        public A(T t){
            a = (T[][]) Array.newInstance(Array.newInstance(List.class,0).getClass(),0);
        }

        public  String t(){
            try {
                System.out.println(A.class.getDeclaredField("c").getGenericType() instanceof WildcardType);
                WildcardType wildcardType = (WildcardType)(((ParameterizedType)A.class.getDeclaredField("b").getGenericType()).getActualTypeArguments()[0]);
                System.out.println(wildcardType.getUpperBounds()[0]);
                System.out.println(A.class.getDeclaredField("a").getType());
                System.out.println(A.class.getDeclaredField("a").getGenericType());
                System.out.println(A.class.getDeclaredField("a").getGenericType() instanceof GenericArrayType);
                GenericArrayType sGenericArrayType = ((GenericArrayType)A.class.getDeclaredField("a").getGenericType());
                Type type = sGenericArrayType;
                Class t0 = getRawType(type);
                Class t = Array.newInstance(t0, 0).getClass();
                System.out.println("type ="+type+" Class =t0="+t0+" t="+t);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return "";
        }

        static Class<?> getRawType(Type type) {
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
                Type componentType = ((GenericArrayType) type).getGenericComponentType();
                return Array.newInstance(getRawType(componentType), 0).getClass();
            }
            if (type instanceof TypeVariable) {
                // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
                // type that's more general than necessary is okay.
                return Object.class;
            }
            if (type instanceof WildcardType) {
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
