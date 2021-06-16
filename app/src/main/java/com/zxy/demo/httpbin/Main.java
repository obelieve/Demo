package com.zxy.demo.httpbin;


import com.google.gson.Gson;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * - 1.GET请求 @GET
 * 	- 1.自定义URL，字节流下载 @Url @Streaming
 * 	- 2.带参数 @QueryMap  ?name=value
 * 	- 3.带参数 @QueryName ?name
 * - 2.POST请求 @POST
 * 	- 2.1 不带参数
 * 	- 2.2 带表单数据 @FormUrlEncoded  @Field
 * 	- 2.3 多部分表格提交 multipart/form-data @Multipart @Part
 * - 3.PATCH请求 @PATCH
 * - 4.DELETE请求 @DELETE
 */
public class Main {

    private static ServiceInterface sServiceInterface = new Retrofit.Builder().baseUrl(ServiceInterface.Companion.getBASE_URL()).client(
            new OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ApiConverterFactory.create())
            .build().create(ServiceInterface.class);

    {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyPort", "8888");
    }

    public static void main(String[] args) throws Exception {
    }

    /**
     * Get请求下载，带有 @Streaming
     */
    @Test
    public void testGetDownload(){
        String url = "https://www.httpbin.org/image/png";
        try {
            Response<ResponseBody> response = sServiceInterface.downloadFile("Range: bytes=0-",url).execute();
            BufferedSink sink = Okio.buffer(Okio.sink(new File("C:\\Users\\Administrator\\Desktop", "testImage.png")));
            sink.writeAll(response.body().source());
            sink.close();
            System.out.println("req图片");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Get请求,带有@QueryName ?name
     */
    @Test
    public void testGetQueryName(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.get("name").execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    /**
     * Get请求,带有@Query ?name=value&name2=value2
     */
    @Test
    public void testGetQuery(){
        String s = null;
        try {
            Response<HttpBinResponse> response = sServiceInterface.get("名字","内容").execute();
            s = response.body().getData();
            System.out.println("url="+new Gson().fromJson(s,HttpBinResponse.Entity.class).getUrl()+" \n"+s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Post请求
     */
    @Test
    public void testPost(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.post().execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    /**
     * Post请求，表单提交 @FormUrlEncoded @Field
     */
    @Test
    public void testPostFormUrlEncoded(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.post("名字","内容").execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    /**
     * Post请求，多部分表单提交 @Multipart @Part
     */
    @Test
    public void testPostMultipart() {
        RequestBody aa = RequestBody.create("name=11&value=22", MediaType.parse("text/plain"));
        try {
            Response<ResponseBody> response = sServiceInterface.post(aa,new File("C:\\Users\\Administrator\\Desktop\\1.png"),new File("C:\\Users\\Administrator\\Desktop\\2.png")).execute();
            String s = response.body().string();
            System.out.println(s);
            HttpBinResponse.Entity entity = new Gson().fromJson(s, HttpBinResponse.Entity.class);
            String name = entity.getFile().get("name").replace("data:image/png;base64,","");
            File file = new File("C:\\Users\\Administrator\\Desktop\\multipart.png");
            FileOutputStream out = new FileOutputStream(file);
            out.write(Base64.getDecoder().decode(name));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Patch请求（Post请求类似） @FormUrlEncoded @Field
     */
    @Test
    public void testPatch(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.patch("n","va").execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    /**
     * Put请求（Post请求类似） @FormUrlEncoded @Field
     */
    @Test
    public void testPut(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.put("n2","va2").execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

    /**
     * Delete请求
     */
    @Test
    public void testDelete(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.delete().execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }

}
