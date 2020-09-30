package com.github.obelieve.main;

import java.util.List;

public class HomeTab {
    private List<HomeTabMenus> menus;

    public static class HomeTabMenus {
        private String menu_ident;

        private String menu_name;

        private String menu_ico_name;

        private int menu_is_hit;

        private int menu_need_login;

        private String menu_url;

        private int menu_number;

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

    public List<HomeTabMenus> getMenus() {
        return menus;
    }

    public void setMenus(List<HomeTabMenus> menus) {
        this.menus = menus;
    }
}
