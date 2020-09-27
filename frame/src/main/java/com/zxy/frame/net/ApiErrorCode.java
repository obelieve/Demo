package com.zxy.frame.net;

/**
 * Created by Admin
 */
public interface ApiErrorCode {

    /**
     * 下载请求错误
     */
    int CODE_DOWNLOAD_INIT = -3;
    /**
     * JSON解析错误
     */
    int CODE_JSON_SYNTAX_EXCEPTION = -2;
    /**
     * 未知错误
     */
    int CODE_UNKNOWN = -1;
    /**
     * 成功
     */
    int CODE_OK = 200;
    /**
     * 操作失败
     */
    int CODE_ERROR = 201;

    /**
     * token失效
     */
    int CODE_TOKEN_ERROR = 202;

    /**
     * 网络连接失败
     */
    int CODE_NET_ERROR = 1001;

    /**
     * HTTP协议错误
     */
    int CODE_HTTP_ERROR = 1002;

    /**
     * 社区模块-昵称重复
     */
    int CODE_DUPLICATE_NICKNAME = 20001;

    /**
     * 社区模块-尚未设置昵称
     */
    int CODE_NOSET_NICKNAME = 20002;

    /**
     * 社区模块-敏感词
     */
    int CODE_SENSITIVE_WORDS = 20003;

    /**
     * 社区模块-帖子\评论\回复发布的时间间隔
     */
    int CODE_TIME_INTERVAL = 20004;
    /**
     * 你还没有绑定手机号
     */
    int CODE_NOBINGD_PHONE = 20005;

    /**
     * 过长昵称
     */
    int CODE_TOOLONG_NICKNAME = 20006;

    /**
     * 账号被封禁
     */
    int CODE_ACCOUNT_BAN = 30002;

    /**
     * 您的账号未绑定手机号 无法申请账号注销
     * 您已申请过账户注销 请等待审核
     */
    int CODE_DONOT_CANCEL = 30006;

}
