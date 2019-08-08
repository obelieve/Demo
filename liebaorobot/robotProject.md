
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
- 设备信息
    - 1.屏幕 1200px * 1920px
    - 2.基于Android 7.1
    - 3.存储 4G/64G
    - 4.远场语音OS:语音识别
- 术语
	- 1.唤醒：控制头部云台转动角度(<45度只会转动云台，大于会和下方一直转)
	- 2.语音识别回调SKillApi#registerCallBack()是显示任何语音的识别结果；和服务器连接指令的回调
	- RobotApi.getInstance().setCallback()不冲突。
	- 3.人脸识别焦点跟随=唤醒+人脸识别(根据人脸识别的方向进行转动)。
- RobotApplication
	- 初始化所有任务模式
		- IDLE_MODE 	//空闲模式
		    - start
		        - 1.页面切换到LeadFragment
		        - 2.skillApi.setRecognizeMode(false)//取消语音连续识别
		        - 3.地图存在，进行导航：RobotApi.getInstance().startNavigation(..)
		        - 4.地图不存在，语音提示无地图
		    - stop
		        - 1.关闭主页面
		        - 2.关闭导航
		        - 3.重新设置为欢迎模式
		- WELCOMD_MODE 	//欢迎模式
		    - start
		        - 1.页面切换到WelcomeFragment
		        - 2.skillApi.setRecognizeMode(true);//设置语音连续识别
		        - 3.RobotApi.getInstance().startGetAllPersonInfo(..)//开始人脸识别
		    - update
		        - 1.更新问题和答案 文本显示
		        - 2.播放答案
		        - 3.人脸检测超时停止判断
		    - stop
		        - 1.RobotApi.getInstance().stopGetAllPersonInfo(..)//停止人脸识别
		        - 2.skillApi.setRecognizeMode(false);//取消语音连续识别
		        - 3.关闭主页面
		- LEADING_MODE 	//导航模式
	- LauncherActivity
		- SkillListFragment //列表
			- 启动ModuleService
				- RobotApi.getInstance().connectServer(...)//服务器连接
					- 连接成功
						- RobotApi.getInstance().setCallback(ModuleCallback)
						- RobotApi.getInstance().registerStatusListener(...) //注册状态监听
						- 启动SpeechService
							- SkillApi.connectApi(..)//服务器连接
							- SkillApi.addApiEventLisener(...)//监听连接
			- 注册EventBus观察者
		- NavigationFragment //导航功能
			- NavigationSkill
				- 1.RobotApi.getInstance().isRobotEstimate(..)//当前定位状态
				- 2.RobotApi.getInstance().getPosition(..)//机器人当前位置坐标点
				- 3.RobotApi.getInstance().setLocation(..)//设置当前位置名称
				- 4.RobotApi.getInstance().removeLocation(..)//删除当前位置点
				- 5.RobotApi.getInstance().getLocation(..)//获取指定位置坐标
				- 6.NavigationSkill.getInstance().goPosition(..)//导航到指定位置坐标
				- 7.RobotApi.getInstance().stopGoPosition(..)//结束导航
				- 8.RobotApi.getInstance().isRobotInlocations()//是否在指定位置
				- 9.NavigationSkill.getInstance().setPoseEstimate(..)//将上面坐标设置定位点
				- 10.RobotApi.getInstance().saveRobotEstimate(..)//将当前坐标设为定位点
				- 11.avigationSkill.getInstance().startNavigation(..)//导航到指定位置
				- 12.RobotApi.getInstance().stopNavigation(..)//停止导航到指定位置
				- 13.RobotApi.getInstance().resumeSpecialPlaceTheta(..)//转向目标点方向
		- FaceFragment //视觉引领
			- FaceSkill
				- RobotApi.getInstance().startRegister(..)//人脸注册
				- RobotApi.getInstance().stopRegister(..)//停止注册
				- RobotApi.getInstance().startLead(..)//开始引导
				- RobotApi.getInstance().stopLead(..)//结束引导
				- RobotApi.getInstance().startFocusFollow(..)//开始焦点跟随
				- RobotApi.getInstance().stopFocusFollow(..)//停止焦点跟随
				- RobotApi.getInstance().switchCamera(..)//前/后置摄像头切换
				- RobotApi.getInstance().getPictureById(..)//获取人脸信息
				- RobotApi.getInstance().startGetAllPersonInfo(..)//开始识别人脸
				- RobotApi.getInstance().stopGetAllPersonInfo(..)//停止识别人脸
		- MoveFragment //位置移动
			- RobotApi.getInstance().goForward(..)//前进
			- RobotApi.getInstance().goBackward(..)//后退
			- RobotApi.getInstance().turnLeft(..)//左转
			- RobotApi.getInstance().turnRight(..)//右转
			- RobotApi.getInstance().motionArc(..)//弧线运动
			- RobotApi.getInstance().moveHead(..)//绝对运动，hmode="absolute"
			- RobotApi.getInstance().moveHead(..)//相对运动, hmode="relative"
			- RobotApi.getInstance().resetHead(..)//恢复云台初始角度
			- RobotApi.getInstance().stopMove(..)//停止运动
		- SpeechFragment //语音功能
			- SpeechSkill
				- RobotApi.getInstance().stopWakeUp(..)//停止唤醒
				- skillApi.setRecognizeMode(true/false)//识别模式
				- skillApi.playText(..)//文字转语音播放
				- skillApi.stopTTS(..)//停止播放
				- skillApi.setRecognizable(true/false)//开启/关闭语音识别
				- skillApi.setASREnabled(true/false)//开启/关闭语音
				- skillApi.setAngleCenterRange(..)//设置语音识别范围
		- MapFragment //地图巡逻
			- NavigationSkill
				- RobotApi.getInstance().switchMap(..)//切换地图
				- RobotApi.getInstance().getMapName(..)//获取当前地图名称
				- RobotApi.getInstance().getPlaceList(..)//获取位置列表
				- RobotApi.getInstance().startCruise(..)//开始巡逻
				- RobotApi.getInstance().stopCruise(..)//停止巡逻
		- ChargeFragment //充电功能
			- ChargeSkill
				- RobotApi.getInstance().setStartChargePoseAction(..)//设置充电桩
				- RobotApi.getInstance().startNaviToAutoChargeAction(..)//设置自动回充
				- RobotApi.getInstance().stopAutoChargeAction(..)//结束自动回充
				- RobotApi.getInstance().stopChargingByApp()//离开充电并脱离充电桩，OTA4.6以上rom
		- SystemFragment //系统功能
			- SystemSkill
				- RobotApi.getInstance().getRobotSn(..)//获取SN号
				- RobotApi.getInstance().textToMp3(..)//文本转mp3文件
			- MediaPlayer播放mp3文件
			- startActivity(PackageManager#getLaunchIntentForPackage(Definition.MODULE_PACKAGE_NAME))//回到豹小秘
				- RobotApi.getInstance().installApk(..)//安装apk
		- 场景案例：关闭当前模式，重新设置欢迎模式。
		```java
		LauncherActivity启动
		-> ModuleService启动(前台)
			->有网络回调初始化
			->ModuleCallback(回调)	***【等待】
				->MessageManager(回调)【等待，指令处理回调】
			->SpeechService启动(语音连接)
			->EventBus回调		***【等待】
			->加载SkillListFragment
				->NavigationFragment(接口调用，监听回调)
				...类似
				->场景案例切换到
					->WelcomeFragment(欢迎模式，人脸检索+加语音识别问答模式)
						->通过导航命令跳转到LeadFragment(导航模式，导航目的地，完成后返回到欢迎模式)
					->通过停止命令（欢迎模式->空闲模式 或 导航模式->欢迎模式）
		```
- 流程
	- 导航功能 
		- 1.当前定位状态
		- 2.当前位置坐标点
		- 3.设置当前位置名称
		- 4.删除当前位置点
		- 5.获取指定位置坐标
		- 6.是否在指定位置
		- 7.转向目标点方向 *
		- 8.导航到指定坐标位置 *
		- 9.结束导航 *
		- 10.导航到指定位置 *
		- 11.停止导航到指定位置 *
		- 12.设置坐标为定位点
		- 13.设置当前坐标点为定位点
	- 人脸识别
		- 1.注册
		- 2.结束注册
		- 3.开始引领 *
		- 4.结束引领 *
		- 5.开始焦点跟随
		- 6.停止焦点跟随
		- 7.切换摄像头
		- 8.开始识别人脸
		- 9.停止识别人脸
	- 位置移动
		- 1.前进 *
		- 2.后退 *
		- 3.左转 *
		- 4.右转 *
		- 5.绝对运动 *
		- 6.相对运动 *
		- 7.恢复云台初始角度 *
		- 8.停止运动 *
	- 语音功能
		- 1.唤醒
		- 2.停止唤醒
		- 文字转语音功能测试
		  - 1.开始播放
		  - 2.停止播放
		- 3.开始语音识别
		- 4.停止语音识别
		- 5.开启语音
		- 6.关闭语音
		- 7.设置语音识别区域
	- 地图巡逻
		- 1.切换地图 
		- 2.获取当前地图名称
		- 3.获取位置列表
		- 4.开始巡逻 *
		- 5.结束巡逻 *
	- 充电功能
		- 1.自动回充 *
		- 2.结束自动回充 *
		- 3.停止充电并脱离充电桩（延迟5s执行)*
	- 系统功能
		- 1.获取SN号
		- 2.文本转MP3
		- 3.播放MP3文件
		- 4.设置灯效
		- 5.回到豹小秘
		- 6.安装APK
