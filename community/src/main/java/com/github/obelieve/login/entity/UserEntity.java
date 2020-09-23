package com.github.obelieve.login.entity;


import com.zxy.frame.proguard.UnProguard;

public class UserEntity implements UnProguard {

    public int user_id; //用户id
    public String avatar;//用户头像
    public String nickname;///用户昵称
    public String username;//手机号
    public int sex;// 用户性别 1男2女0保密
    public String imid;///用户云信id
    public String imtoken;//云信token
    public String birthday;//用户生日
    public String token;//认证token
    public String open_id;//第三方登录时使用
    public String do_type; //登录还是注册 login:登录；register:注册 wechat:微信（未绑定手机号）；sina:新浪微博（未绑定手机号）；qq:QQ（未绑定手机号）
    public int follow;//用户关注数
    public int fans;
    public int activedays;//用户活跃天数
    public String provinceid;//省份标识
    public String province_zh;//省份名称
    public String city;//城市标识
    public String city_zh;//城市名称
    public String age;//用户年龄
    public int notice_num;//系统通知数
    public int message_num;//我的消息数
    public String userinfo;//
    public HomeTeamEntity home_team;
    public String mobile;//手机号码
    public String alias;//别名


    public String getUserInfo() {
        return userinfo;
    }

    public static class HomeTeamEntity implements UnProguard {
        public int team_id; //主队ID
        public String logo;//主队LOGO
        public String name_zh;///主队名称
    }

}
