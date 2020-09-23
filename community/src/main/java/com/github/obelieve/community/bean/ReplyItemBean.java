package com.github.obelieve.community.bean;

public class ReplyItemBean {
    /**
     * user_id : 1
     * nickname : 罗纳多多
     * avatar : https://image.ssUnVDP1565010581
     * reply_id : 8
     * reply_time : 15小时前
     * content : 你过来呀🍤
     * zan_num : 1
     * comment_id : 3
     * post_id : 5
     * to_uid : 122
     * to_uname : 取个名字1
     * is_zan : 0
     * is_del : 1
     * is_report : 1
     */

    private int user_id;
    private int user_level;
    private String nickname;
    private String avatar;
    private int reply_id;
    private String reply_time;
    private String content;
    private int zan_num;
    private int comment_id;
    private int post_id;
    private int to_uid;
    private String to_uname;
    private int is_zan;
    private int is_del;
    private int is_report;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
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

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZan_num() {
        return zan_num;
    }

    public void setZan_num(int zan_num) {
        this.zan_num = zan_num;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
    }

    public String getTo_uname() {
        return to_uname;
    }

    public void setTo_uname(String to_uname) {
        this.to_uname = to_uname;
    }

    public int getIs_zan() {
        return is_zan;
    }

    public void setIs_zan(int is_zan) {
        this.is_zan = is_zan;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getIs_report() {
        return is_report;
    }

    public void setIs_report(int is_report) {
        this.is_report = is_report;
    }
}

