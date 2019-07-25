# 机器人项目

### 备注
1.机器人资料-接口以及demo下:
- MyDemo：调用ifeeling库(TTS、usb_printer等)、EventBus发送MQServiceTask命令。
- MyDemo20170626：和MyDemo类似。


### HRT_SP_ROBOT目录
```
F:\ZXY_WORKSPACE\WORK\HRT_SP_ROBOT
├─01_trunk
│  ├─01_项目文档
│  └─02_代码 //Android 代码
│      ├─GDS 
├─02_branches
│  └─demo
│      ├─01_项目文档
│      └─02_代码 //Web 代码
│          └─solr_project
│            
└─03_tags
```
#### GDS项目
- 哈工大建行机器人
	- 基于ZeroMQ的通信协议
		- EventName String	事件名称
		- Sender 	String	事件发送方（是由哪个模块发的）
		- Priority 	String	优先级 	 （时间优先级）
		- Data  	JSON 	事件内容
	- Navi++
	- 功能模块
		- 1.底盘 base
		- 2.语音识别 asr (语音->文本)
		- 3.语音合成 tts (文本->语音)
		- 4.语音唤醒 iflytek,发送唤醒命令和角度（检测人来时）
		- 5.体感摄像头 kinect，检测人体接近/离开
		- 6.身份证读卡 idcard_reader
		- 7.usb 打印机 usb_printer
		- 8.网络打印机 network_printer
		- 9.手写签名板 signature_pad
		- 10.摄像头 camera
	- API调用：
		- 1.事件绑定监听
			```java
			TaskListenerManager.getManager().bindListenerWithEventName( this, "tts.speak");
			```
		- 2.发送事件：
			```java
			MQServiceTask task = new MQServiceTask("tts.speak");
			JSONObject obj = new JSONObject();
			obj.put("tts.content","您好啊");
			task.setData(obj.toString());
			task.setModule("tts");
			MessageEvent event = MessageEvent.create(EventName.SEND_MQ_TASK,task);
			EventBus.getDefault().post(event);
			```
- 依赖包
	- 1.jxl.jar 解析excel的包
	- 2.状态栏 `https://github.com/laobie/StatusBarUtil`
	- 3.集成字体图标：`https://github.com/imtianx/material-icon-lib`
	- 4.分组列表RecyclerView适配器：`com.github.donkingliang:GroupedRecyclerViewAdapter`
	- 5.电子签名：`https://github.com/gcacace/android-signaturepad`
	- 6.http请求库httpmime：`org.apache.httpcomponents:httpmime`
	- 7. http请求httpclient：`org.apache.httpcomponents:httpclient-android:4.3.5`
	- 8.Zloading动画加载库：`https://github.com/zyao89/ZLoading`
- 主要类
	- ZeroMQ:ZMQ，是一个消息队列库
	- MyApplication：把assets一些文件读取，初始化机器人库、启动服务。
	- SettingManager：读写SP
	- MattersDBManager：把asserts下的审批事项.xls表格通过jxl包解析，存放到数据库中，然后再从数据库读取这个数据存放到List<MattersInfo>。
	- MattersDBHelper：数据库建表语句
	- MatterConfig：数据库字段存放处。
	- 机器人依赖库初始化
		- 1.MyApp.init(this)
		- 2.启动MQProxyService、ExternalDeviceService
	- ConfigurationFile：
	检查sdcard、
	创建GDS/gds.json(Solr服务配置信息)、
	isPrintingSpeak.json(是否打印语言文件信息)、
	asrkeyword.json（assets/下的文件）
	- BatteryBroadCastReceiver：监控电量，用于发送/撤销充电指令。
	- SlptServiceInstalledReceiver：开机启动应用
	- ScreenRelativeLayout#onMeasure 根据实际宽高与1920x1080差别，对控件宽高比例进行放大/缩小
	- 主要页面
		- SplashActivity:
			- 1.延时轮播图
			- 2.获取POI点
			- 3.固定时间到了，发送广播"com.hrt.service"
			- 4.进行App更新检测
			- 5.更新底盘电池状态
		- MainActivity:
		
##### 项目原型
- 名称：宁德市行政服务中心机器人（展示、取号、咨询功能）
- （可进行语音识别）
```
我要办xxx/我要取号
发改委
xxx许可证核发
```
- 排队取号
	- 1.选择办理事项(按部门分类-子类项)
	- 2.刷身份证取号（第一次需要输入手机号）
- 办理什么事？
- 1.显示办事指南
- 2.取号
- 3.导航（当前所处位置）、带我去【机器导航】
- 4.呼叫人工
##### 需求规格（业务流程）
```
办事人员->办理某事（语音识别）->检索事项表
					->否 相关提示（未找到html）
					->是
						->办事指南 
						->取号
						->导航->位置导航(导航)
						->不是此项->读取其他相关事项再判断
						->呼叫人工->人工服务
				
		
```

### 优必选机器人Demo
- skills:使用ROSA调用语音、表情服务的例子；
- overriding-speech-service:语音覆盖配置，识别/合成/理解进行覆盖服务；
- calling-system-services:具体调用例子；

## 猎豹机器人