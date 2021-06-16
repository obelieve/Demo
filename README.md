# Retrofit 源码分析

## 1.用法
```java
- 1.GET请求
	- 1.带参数
	- 2.字节流下载
- 2.POST请求
	- 2.1 不带参数
	- 2.2 带参数
	- 2.3 多部分表格提交 multipart/form-data
```
## 2.问题
- 1.Retrofit是怎么选择转换器和回调适配器的？ 1.Converter#requestBodyConverter(..) return !=null，CallAdapter#get(..) return !=null
- 2.怎么配置动态BaseURL，在哪个地方可以修改这个东西，Retrofit什么时候拼接URL进行请求的？
## 3.前置知识
- 1.Http协议部分
	- 1.报文：
	```
	请求行： 请求方法 URL HTTP/大版本.小版本 \r\n
	请求头： Name:Value \r\n
	\r\n
	请求体:
	```
	```
	响应行： HTTP/大版本.小版本 响应码 响应信息 \r\n
	响应行：	Name:Value \r\n
	\r\n
	响应体:
	```
	- 2.请求方法：
		- Post、Put、Patch 增/改
		- Delete 删
		- Get、Head、Trace(响应时会返回报文追踪的信息.额外的响应头) 查
		- Options 返回服务器支持的请求方法有哪些
	- 3.多部分表单提交multipart/formdata
	```
	使用boundary切分内容
	Content-Type: multipart/form-data; boundary=xxx
	xxx
	Content-Disposition: form-data; name="submit-name"
	内容
	xxx
	Content-Disposition: form-data; name="files"; filename="essayfile.txt"
	Content-Type: text/plain
	内容
	xxx
	```
- 2.OKHttp的HttpUrl部分
	```
	HttpUrl#resolve(String)：重新解析更新HttpUrl，如果是绝对地址HttpUrl更新为新的绝对地址。
	```
- 3.Okio部分：Source和Sink 对应 InputStream和OutputStream
## 4.源码分析
- 1.解析接口方法，获得参数数据并生成一个OKHttp的Request对象
	- 1.解析方法的注解
		- @POST('relativeUrl')
		- @PUT('relativeUrl')
		- @PATCH('relativeUrl')
		- @GET('relativeUrl')
		- @HEAD('relativeUrl')
		- @OPTIONS('relativeUrl')
		- @HTTP(httpMethod,'relativeUrl')
		- @Headers 请求头解析
		- @Multipart 多部分表单提交
		- @FormUrlEncoded 表单提交
	- 2.解析方法参数的注解
		- @Url //relativeUrl
		- @Path //@Url注解中的{name}替换的值
		- @Query //?name=value
		- @QueryName //?name，没有value
		- @QueryMap //?name=value&name2=value2
		- @Header //请求头
		- @HeaderMap //多个请求头
		- @Field //表单提交键值对
		- @FieldMap //表单提交多个键值对
		- @Part //多表单提交，其中数据类型通过Converter会转为RequestBody
		- @PartMap //多表单提交，多个RequestBody
		- @Body //自定义请求包体，其中数据类型会转为RequestBody
		- @Tag //请求对象的tag标识
	- 3.拼装数据生成Request对象
- 2.选择回调适配器
- 3.选择响应数据转换器
## 5.疑难
- 1.Type#getType()和Type#getGenericType()区别：
  如果不是泛型就相同，如果是泛型的话，Type#getGenericType()会返回泛型类型。
- 2.对于super和extends的上界下界：
  ```
  List<? super Food> 表示Food的超类，说明Food是?的下界
  List<? extends Fruit> 表示Fruit的子类，说明Fruit是?的上界

  class Food{}
  class Fruit extends Food {}
  ```

  https://app.diagrams.net/