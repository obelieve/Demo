package com.github.obelieve.community.bean;

import java.util.List;

/**
 * 评论回复列表
 */
public class ReplyListEntity {

    /**
     * current_page : 1
     * data : [{"user_id":1,"nickname":"罗纳多多","avatar":"https://image.ssUnVDP1565010581","reply_id":8,"reply_time":"15小时前","content":"你过来呀🍤","zan_num":1,"comment_id":3,"post_id":5,"to_uid":122,"to_uname":"取个名字1","is_zan":0,"is_del":1,"is_report":1}]
     * from : 1
     * last_page : 1
     * per_page : 10
     * to : 5
     * total : 5
     */

    private int current_page;
    private int from;
    private int last_page;
    private int per_page;
    private int to;
    private int total;
    private List<ReplyItemBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ReplyItemBean> getData() {
        return data;
    }

    public void setData(List<ReplyItemBean> data) {
        this.data = data;
    }


}
