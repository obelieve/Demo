package com.github.obelieve.me.bean;


import com.zxy.frame.proguard.UnProguard;

public class MenuDataEntity implements UnProguard {
    public String menu_ident;// notice,   //菜单唯一标识 notice-系统公告；message-我的消息；feedback-问题反馈；setup-系统设置
    public String menu_name;// 系统公告,   //菜单名称
    public String menu_ico_name;// my_the_announcement,   //菜单图标名称
    public int menu_is_hit;// 1,    //菜单是否可点击 1是0否
    public int menu_need_login;// 1,    //是否需要登录 1是0否
    public String menu_url;// 菜单是否H5 空则不是。非空则是，点击需跳转至对应url
    public int menu_number;// 1    //未读数；0则不显示

    public String getMenu_ident() {
        return menu_ident;
    }

    public void setMenu_ident(String menu_ident) {
        this.menu_ident = menu_ident;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_ico_name() {
        return menu_ico_name;
    }

    public void setMenu_ico_name(String menu_ico_name) {
        this.menu_ico_name = menu_ico_name;
    }

    public int getMenu_is_hit() {
        return menu_is_hit;
    }

    public void setMenu_is_hit(int menu_is_hit) {
        this.menu_is_hit = menu_is_hit;
    }

    public int getMenu_need_login() {
        return menu_need_login;
    }

    public void setMenu_need_login(int menu_need_login) {
        this.menu_need_login = menu_need_login;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public int getMenu_number() {
        return menu_number;
    }

    public void setMenu_number(int menu_number) {
        this.menu_number = menu_number;
    }
}
