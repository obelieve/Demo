![流程图.png](https://github.com/obelieve/Demo/blob/code-retrofit/%E6%B5%81%E7%A8%8B%E5%9B%BE.png)
## 1. 介绍
   Retrofit主要是基于OKHttp进一步封装的网络请求的框架。它采用了一种接口方法声明请求的方式，使用方法注解和方法参数注解进行标记请求信息，通过动态代理访问接口方法并解析Method，最后将请求信息汇总组装成okhttp3.Request对象，再根据接口方法返回值类型决定调用适配器(CallAdapter)，内部通过okhttp3.Call.enqueue(..) 或okhttp3.Call.execute() 同步/异步请求，请求响应后通过转换器(Converter)把okhttp3.ResponseBody数据再进行一次转换得到最后的数据。

## 2.用法
这一小节通过一系列不同请求方式的代码来展示相关的用法。
主要分为6个文件，具体代码文件在最后面。
-  ServiceInterface.kt 声明接口请求
-  Main.kt 测试接口请求
-  ApiConverterFactory.java 转换器工厂类
-  ApiRequestBodyConverter.java  转为RequestBody对象转换器类 （@Part 参数对象转为RequestBody）
-  ApiResponseBodyConverter.java  ResponseBody对象转换器类
-  HttpBinResponse.java  ResponseBody装好后的数据类
### 2.1 基本用法
 - 1.GET 请求下载文件
 - 2.GET 请求
 - 3.GET 请求 查询?name=value
 - 4.GET 请求 查询?name
 - 5.POST 请求
 - 6.POST 请求  表单数据提交 name="名字"&content="内容"
 - 7.POST 请求  多部分表单提交 multipart/form-data
### 2.2 进阶用法
- 1. 通过@URL 请求一个不同地址的URL
- 2. 通过Interceptor拦截器，实现动态设置不同BaseUrl


## 3. 源码
    这一小节，通过一个post表单提交梳理下执行流程。以下是这个post请求的代码：分为三部分
- 1. 接口请求声明部分
- 2. Retrofit类构建，并返回接口的实现类部分
- 3. 接口请求测试部分
```
interface ServiceInterface {
...
companion object {
        var BASE_URL = "http://www.httpbin.org/"
    }
@POST("/post")
    @FormUrlEncoded
    fun post(@Field("name") name:String,@Field("content") content:String): Call<ResponseBody>
...
}
public class Main {
...
private static ServiceInterface sServiceInterface
 = new Retrofit.Builder()
.baseUrl(ServiceInterface.Companion.getBASE_URL())
.client(new OkHttpClient.Builder().build())
.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
.addConverterFactory(ApiConverterFactory.create())
.build()
.create(ServiceInterface.class);
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
...
}
```
从`sServiceInterface.post("名字","内容").execute();`开始分析
1.先看看`sServiceInterface`是怎么创建的?
2.再看看调用`post("名字","内容")`方法是怎么回事?
3.最后看下执行`execute()`是怎么样的？

#### 开始分析1.
```java
//先进入Retrofit#create(..)方法
public <T> T create(final Class<T> service) {
    return (T)
        Proxy.newProxyInstance(
            service.getClassLoader(),
            new Class<?>[] {service},
            new InvocationHandler() {
            ...
              @Override
              public @Nullable Object invoke(Object proxy, Method method, @Nullable Object[] args)
                  throws Throwable {
               ...
                return platform.isDefaultMethod(method)
                    ? platform.invokeDefaultMethod(method, service, proxy, args)
                    : loadServiceMethod(method).invoke(args); //看重点部分
              }
            });
//分析1结束，通过Proxy.newProxyInstance(..)动态代理获取接口ServiceInterface代理对象。
```
#### 开始分析2.
从上一步的`loadServiceMethod(method).invoke(args)`开始分为2个部分：
- 2.1 `loadServiceMethod(method)`
- 2.2 调用`invoke(args)`方法

##### 开始分析2.1 `loadServiceMethod(method)`
```java
 //从Retrofit#loadServiceMethod(..)开始
  ServiceMethod<?> loadServiceMethod(Method method) {
       ...
        result = ServiceMethod.parseAnnotations(this, method);
       ...
    }
    return result;
  }
//ServiceMethod#parseAnnotations(..)
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    //解析生成RequestFactory对象，具体流程在【流程图】图片有描述
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);
    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

//HttpServiceMethod.parseAnnotations(..)
  static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(
      Retrofit retrofit, Method method, RequestFactory requestFactory) {
    ...
      adapterType = method.getGenericReturnType();
    ...
  //获取调用适配器CallAdapter对象，具体流程在【流程图】图片有描述
    CallAdapter<ResponseT, ReturnT> callAdapter =
        createCallAdapter(retrofit, method, adapterType, annotations);
    Type responseType = callAdapter.responseType();
    ...
  //获取转换器Converter对象，具体流程在【流程图】图片有描述
    Converter<ResponseBody, ResponseT> responseConverter =
        createResponseConverter(retrofit, method, responseType);
  ...
  okhttp3.Call.Factory callFactory = retrofit.callFactory;
  return new CallAdapted<>(requestFactory, callFactory, responseConverter, callAdapter);
  ...
  }
/**CallAdapted extends HttpServiceMethod<ResponseT, ReturnT>
HttpServiceMethod<ResponseT, ReturnT> extends ServiceMethod<ReturnT>
**/
//最后返回CallAdapted对象，分析2.1结束。
```
##### 开始分析 2.2  调用`invoke(args)`方法
```java
/**CallAdapted中没有invoke方法，invoke方法的实现位于它的父类HttpServiceMethod中**/
//HttpServiceMethod#invoke(..)
 @Override
  final @Nullable ReturnT invoke(Object[] args) {
    Call<ResponseT> call = new OkHttpCall<>(requestFactory, args, callFactory, responseConverter);
    return adapt(call, args);
  }
//adapt(call, args)方法的实现位于CallAdapted中
//CallAdapted#adapt(..)
    @Override
    protected ReturnT adapt(Call<ResponseT> call, Object[] args) {
      return callAdapter.adapt(call);
    }
/**调用到CallAdapter#adapt(call)方法，由于之前请求接口方法返回值是Call<ResponseBody>，所以调用DefaultCallAdapterFactory#get(..)返回callAdapter对象
**/
//DefaultCallAdapterFactory#get(..)
  @Override
  public @Nullable CallAdapter<?, ?> get(
      Type returnType, Annotation[] annotations, Retrofit retrofit) {
    ...
    return new CallAdapter<Object, Call<?>>() {
      @Override
      public Type responseType() {
        return responseType;
      }
      @Override
      public Call<Object> adapt(Call<Object> call) {
        return executor == null ? call : new ExecutorCallbackCall<>(executor, call);
      }
    };
  }
//最后返回ExecutorCallbackCall对象，分析2.2结束
```
#### 开始分析3.
```java
//从上一步最后返回ExecutorCallbackCall对象，开始分析
//在ExecutorCallbackCall类中
...
   ExecutorCallbackCall(Executor callbackExecutor, Call<T> delegate) {
      this.callbackExecutor = callbackExecutor;
      this.delegate = delegate;
    }
//ExecutorCallbackCall#execute()
    @Override
    public Response<T> execute() throws IOException {
      return delegate.execute();
    }
//由上可知delegate是OkHttpCall对象
//OkHttpCall#execute()
  @Override
  public Response<T> execute() throws IOException {
    okhttp3.Call call;
    ...
      //获取okhttp3.Call对象，具体流程在【流程图】图片有描述
      call = getRawCall();
    }
    ...
    return parseResponse(call.execute());
  }
//OkHttpCall#parseResponse(call.execute())
  Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
    ResponseBody rawBody = rawResponse.body();
    ...
    ExceptionCatchingResponseBody catchingBody = new ExceptionCatchingResponseBody(rawBody);
    try {
      //Converter转换器转换数据，然后返回
      T body = responseConverter.convert(catchingBody);
      return Response.success(body, rawResponse);
    } catch (RuntimeException e) {
      catchingBody.throwIfCaught();
      throw e;
    }
  }
//最后Converter转换器转换数据，然后返回retrofit2.Response对象，分析3结束。
...
```
### 总结下：
`sServiceInterface.post("名字","内容").execute();`
1.`sServiceInterface`通过Proxy.newProxyInstance(..)动态代理获取接口ServiceInterface代理对象。
2.调用`post("名字","内容")`方法时，开始执行动态代理内部方法，通过层层解析返回ExecutorCallbackCall类，其中包含OkHttpCall对象变量，OkHttpCall中主要包含了一些okhttp3.Request、okhttp3.Call.execute()等处理。
3.执行`execute()`，最后就是执行了okhttp3.Call.execute()。
---

## 4.其他
- 1.对于super和extends的上界下界：
  ```
  List<? super Food> 表示Food的超类，说明Food是?的下界 （只能add，不能get）
  List<? extends Fruit> 表示Fruit的子类，说明Fruit是?的上界 （只能get，不能add）

  class Food{}
  class Fruit extends Food {}
  ```

`【2】用法源码部分`
```kotlin
interface ServiceInterface {

    companion object {
        var BASE_URL = "http://www.httpbin.org/"
    }

    @Streaming
    @GET
    fun downloadFile(
        @Header("RANGE") downParam: String,
        @Url fileUrl: String
    ): Call<ResponseBody>

    @GET("/")
    fun get(): Call<ResponseBody>

    @GET("/get")
    fun get(@Query("name")name:String,@Query("content")content: String): Call<HttpBinResponse>

    @GET("/get")
    fun get(@QueryName name:String): Call<ResponseBody>

    @POST("/post")
    fun post(): Call<ResponseBody>

    @POST("/post")
    @FormUrlEncoded
    fun post(@Field("name") name:String,@Field("content") content:String): Call<ResponseBody>

    @POST("/post")
    @Multipart
    fun post(@Part("va") va:RequestBody,@Part("name\"; filename=\"name.png")name: File, @Part("name2\"; filename=\"name2.png")name2: File): Call<ResponseBody>
}
```
```kotlin
public class Main {

    private static ServiceInterface sServiceInterface = new Retrofit.Builder().baseUrl(ServiceInterface.Companion.getBASE_URL()).client(
            new OkHttpClient.Builder().addInterceptor(new Interceptor() {

                private volatile String host;

                public void setHost(String host) {
                    this.host = host;
                }

                @NotNull
                @Override
                public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                    String host = this.host;
                    if (host != null) {
                        HttpUrl newUrl = request.url().newBuilder().host(host).build();
                        request = request.newBuilder().url(newUrl).build();
                    }
                    return chain.proceed(request);
                }
            }).build())
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
     * Get请求,
     */
    @Test
    public void testGet(){
        String s = null;
        try {
            Response<ResponseBody> response = sServiceInterface.get().execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s);
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
}
```
```java
public class ApiConverterFactory extends Converter.Factory{

    public static ApiConverterFactory create(){
        return new ApiConverterFactory();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new ApiRequestBodyConverter();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ApiResponseBodyConverter();
    }
}
```
```java
public class ApiRequestBodyConverter implements Converter<File, RequestBody> {
    @Nullable
    @Override
    public RequestBody convert(File value) throws IOException {
        if(value.getAbsolutePath().contains(".png")){
            RequestBody bodyParams = RequestBody.create(value,MediaType.parse("image/png"));
            return bodyParams;
        }
        return null;
    }
}
```
```java
public class ApiResponseBodyConverter implements Converter<ResponseBody, HttpBinResponse> {

    @Nullable
    @Override
    public HttpBinResponse convert(ResponseBody value) throws IOException {
        String json = value.string();
        HttpBinResponse response = new HttpBinResponse();
        try{
            response.setCode(1);
            response.setMessage("Success");
            response.setData(json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
```
```java
public class HttpBinResponse {

    private String message;
    private int code;
    private Entity entity;
    private String data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class Entity{
        private Object args;
        private String data;
        private Map<String,String> files;
        private Map<String,String> form;
        private Map<String,String> headers;
        private String json;
        private String origin;
        private String url;

        public Object getArg() {
            return args;
        }

        public void setArg(Object arg) {
            args = arg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Map<String,String> getFile() {
            return files;
        }

        public void setFile(Map<String,String> file) {
            files = file;
        }

        public Map<String, String> getForm() {
            return form;
        }

        public void setForm(Map<String, String> form) {
            this.form = form;
        }

        public Map<String, String> getHeader() {
            return headers;
        }

        public void setHeader(Map<String, String> header) {
            headers = header;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "args=" + args +
                    ", data='" + data + '\'' +
                    ", files=" + files +
                    ", form=" + form +
                    ", headers=" + headers +
                    ", json='" + json + '\'' +
                    ", origin='" + origin + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
```
