package com.github.obelieve.community.bean;


import java.util.List;

public class BBSUserTrendsEntity {

    /**
     * post_list : [{"user_id":122,"nickname":"取个名字1","avatar":"https://image.2048.com/lSjkhHqEOKxUL1569291305","post_id":20,"post_time":"31分钟前","content":"关于苏索的问题请教，他有自己的定位吗","media":{"media_type":"image","media_list":[{"thumbnail":"https://N9N71/200/format/jpg","original":"https://imagk3N9N71xt71566356791"}]},"pc_num":0,"zan_num":0,"is_top":1,"is_zan":1,"is_del":1,"is_report":1}]
     * has_next_page : 1
     */

    private int has_next_page;
    private int current_page;
    private List<SquareListsEntity.PostListBean> post_list;

    public int getHas_next_page() {
        return has_next_page;
    }

    public void setHas_next_page(int has_next_page) {
        this.has_next_page = has_next_page;
    }

    public List<SquareListsEntity.PostListBean> getPost_list() {
        return post_list;
    }

    public void setPost_list(List<SquareListsEntity.PostListBean> post_list) {
        this.post_list = post_list;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
}
