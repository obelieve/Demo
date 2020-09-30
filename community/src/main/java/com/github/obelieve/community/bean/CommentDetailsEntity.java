package com.github.obelieve.community.bean;

public class CommentDetailsEntity {
    private int user_id;

    private String nickname;

    private String avatar;

    private int comment_id;

    private String comment_time;

    private String content;

    private int reply_num;

    private int zan_num;

    private int post_id;

    private int is_zan;

    private int is_del;

    private int is_report;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_id() {
        return this.comment_id;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getComment_time() {
        return this.comment_time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getReply_num() {
        return this.reply_num;
    }

    public void setZan_num(int zan_num) {
        this.zan_num = zan_num;
    }

    public int getZan_num() {
        return this.zan_num;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getPost_id() {
        return this.post_id;
    }

    public void setIs_zan(int is_zan) {
        this.is_zan = is_zan;
    }

    public int getIs_zan() {
        return this.is_zan;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getIs_del() {
        return this.is_del;
    }

    public void setIs_report(int is_report) {
        this.is_report = is_report;
    }

    public int getIs_report() {
        return this.is_report;
    }

}
