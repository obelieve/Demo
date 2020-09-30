package com.github.obelieve.community.bean;

public class BBSUserInfoEntity {


    /**
     * user_id : 1
     * nickname : 罗纳多多
     * avatar : https://image.ssjhUnVDP1565010581
     * follow : -
     * fans : -
     * activedays : 1
     * userinfo : 保密·来自火星·19岁
     * message_num : 0
     * is_self : 0
     */

    private int user_id;
    private String nickname;
    private String avatar;
    private String follow;
    private String fans;
    private String activedays;
    private String userinfo;
    private int message_num;
    private int is_self;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getActivedays() {
        return activedays;
    }

    public void setActivedays(String activedays) {
        this.activedays = activedays;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public int getMessage_num() {
        return message_num;
    }

    public void setMessage_num(int message_num) {
        this.message_num = message_num;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
    }
}
