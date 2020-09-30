package com.github.obelieve.main;

/**
 * 彩经板块 用户信息
 */
public class UserNavInfoEntity {

    /**
     * user_id : 8967
     * nickname : 1213464646
     * username : 2048dev_5e0206b61b77c
     * mobile : 13395080006
     * sex : 1
     * avatar : https://image.2048.com/fBkGj1582626282594.jpg
     * imid : 2048dev_5e0206b61b77c
     * imtoken : 5059fa6e3e8f95be66e37768eaeb37b8
     * red : 32
     * activedays : 41
     * city : 福建省,福州市
     * city_zh : 福建福州
     * birthday : 1999-09-09
     * age : ·20岁
     * message_num : 1
     * userinfo : 男·福建福州·20岁
     * alias : null
     * coin : 32
     * level : 1
     * level_month_coin : 10
     */

    private int user_id;
    private String nickname;
    private String username;
    private String mobile;
    private int sex;
    private String avatar;
    private String imid;
    private String imtoken;
    private String red;
    private String activedays;
    private String city;
    private String city_zh;
    private String birthday;
    private String age;
    private int message_num;
    private String userinfo;
    private String alias;//友盟别名
    private int coin;//金币
    private int level;//用户等级
    private int level_month_coin;//等级每月奖励 0不需要提醒 >0 领取金额

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImid() {
        return imid;
    }

    public void setImid(String imid) {
        this.imid = imid;
    }

    public String getImtoken() {
        return imtoken;
    }

    public void setImtoken(String imtoken) {
        this.imtoken = imtoken;
    }

    public String getRed() {
        return red;
    }

    public void setRed(String red) {
        this.red = red;
    }

    public String getActivedays() {
        return activedays;
    }

    public void setActivedays(String activedays) {
        this.activedays = activedays;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_zh() {
        return city_zh;
    }

    public void setCity_zh(String city_zh) {
        this.city_zh = city_zh;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getMessage_num() {
        return message_num;
    }

    public void setMessage_num(int message_num) {
        this.message_num = message_num;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel_month_coin() {
        return level_month_coin;
    }

    public void setLevel_month_coin(int level_month_coin) {
        this.level_month_coin = level_month_coin;
    }
}
