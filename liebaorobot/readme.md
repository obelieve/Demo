## 猎豹机器人
- 管理平台 `https://jiedai.ainirobot.com/#/home`
- 官方社区 `https://bbs.ainirobot.com/forum.php`
- 二次开发Demo `https://bbs.ainirobot.com/forum.php?mod=viewthread&tid=114`
    - demo下载 `https://pan.baidu.com/s/1vRVROluJNTxIDcbsXfPevA?errno=0&errmsg=Auth%20Login%20Sucess&&bduss=&ssnerror=0&traceid=`
    - demo文档 `https://note.youdao.com/ynoteshare1/index.html?id=9029108f9528c2865b9fc62ca8e887ce&type=note`
    - 欢迎语 `http://nlpdoc.ainirobot.com/#/?id=%e7%ac%ac%e4%b8%80%e7%ab%a0-%e6%ac%a2%e8%bf%8e%e8%af%ad`
- 配置
  - 1.导入依赖包robotserviceV4.10.jar，存放目录下/libs
  - 2.AndroidManifest.xml app默认启动配置
  ```java
  <intent-filter>
   <action android:name="action.orionstar.default.app" />
   <category android:name="android.intent.category.DEFAULT" />
  </intent-filter>
  ```
  - 3.