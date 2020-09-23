package com.github.obelieve.me.bean;


import java.util.List;

/**
 * 获取个人中心导航菜单接口（彩经版本）
 */
public class CenterMenuEntity {

    /**
     * menus : {"menu_up":[{"menu_ident":"plan","menu_name":"已购方案","menu_ico_name":"my_personal_homepage","menu_is_hit":1,"menu_need_login":1,"menu_url":"","menu_number":0}],"menu_down":[{"menu_ident":"notice","menu_name":"系统公告","menu_ico_name":"my_the_announcement","menu_is_hit":1,"menu_need_login":1,"menu_url":"","menu_number":2}]}
     */

    private MenusBean menus;

    public MenusBean getMenus() {
        return menus;
    }

    public void setMenus(MenusBean menus) {
        this.menus = menus;
    }

    public static class MenusBean {
        private List<MenuDataEntity> menu_up;
        private List<MenuDataEntity> menu_down;
        private int red_status;

        public List<MenuDataEntity> getMenu_up() {
            return menu_up;
        }

        public void setMenu_up(List<MenuDataEntity> menu_up) {
            this.menu_up = menu_up;
        }

        public List<MenuDataEntity> getMenu_down() {
            return menu_down;
        }

        public void setMenu_down(List<MenuDataEntity> menu_down) {
            this.menu_down = menu_down;
        }

        public int getRed_status() {
            return red_status;
        }

        public void setRed_status(int red_status) {
            this.red_status = red_status;
        }
    }
}
