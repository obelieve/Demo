package com.github.obelieve.community.bean;

import java.util.List;

public class UpdateDetailEntity {


    /**
     * user_id : 122
     * nickname : 取个名字1
     * avatar : https://image.2048.com/lSjkhHqEOKxUL1569291305
     * post_id : 20
     * post_time : 31分钟前
     * content : 关于苏索的问题请教，他有自己的定位吗
     * media : {"media_type":"image","media_list":[{"thumbnail":"https://N9N71/200/format/jpg","original":"https://imagk3N9N71xt71566356791"}]}
     * pc_num : 0
     * zan_num : 0
     * is_top : 1
     * is_zan : 1
     * is_del : 1
     * is_report : 1
     */

    private int user_id;
    private String nickname;
    private String avatar;
    private int post_id;
    private String post_time;
    private String content;
    private MediaBean media;
    private int pc_num;
    private int zan_num;
    private int is_top;
    private int is_zan;
    private int is_del;
    private int is_report;

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

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MediaBean getMedia() {
        return media;
    }

    public void setMedia(MediaBean media) {
        this.media = media;
    }

    public int getPc_num() {
        return pc_num;
    }

    public void setPc_num(int pc_num) {
        this.pc_num = pc_num;
    }

    public int getZan_num() {
        return zan_num;
    }

    public void setZan_num(int zan_num) {
        this.zan_num = zan_num;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
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

    public static class MediaBean {
        /**
         * media_type : image
         * media_list : [{"thumbnail":"https://N9N71/200/format/jpg","original":"https://imagk3N9N71xt71566356791"}]
         */

        private String media_type;
        private List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list;

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> getMedia_list() {
            return media_list;
        }

        public void setMedia_list(List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list) {
            this.media_list = media_list;
        }


    }
}
