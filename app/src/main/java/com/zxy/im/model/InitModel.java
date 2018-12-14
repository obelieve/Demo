package com.zxy.im.model;

/**
 * Created by zxy on 2018/12/14 10:48.
 */

public class InitModel extends BaseModel
{

    /**
     * data : {"site_logo":"","foot_logo":"","site_title":"IMWEB","copyright_info":"","app_ali_vcode_enabled":0,"version":{"has_upgrade":0,"forced_upgrade":0},"app_down_url":{"ios_down_url":"http://www.eosdapp.co/appdown","android_down_url":"http://www.eosdapp.co/appdown"},"h5_agreement":"http://www.eosdapp.co/article/detail?id=1","h5_statement":"http://www.eosdapp.co/article/detail?id=2","h5_about":"http://www.eosdapp.co/article/detail?id=3","h5_notice_base_url":"http://www.eosdapp.co/notice","h5_article_base_url":"http://www.eosdapp.co/article"}
     */

    private DataBean data;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * site_logo :
         * foot_logo :
         * site_title : IMWEB
         * copyright_info :
         * app_ali_vcode_enabled : 0
         * version : {"has_upgrade":0,"forced_upgrade":0}
         * app_down_url : {"ios_down_url":"http://www.eosdapp.co/appdown","android_down_url":"http://www.eosdapp.co/appdown"}
         * h5_agreement : http://www.eosdapp.co/article/detail?id=1
         * h5_statement : http://www.eosdapp.co/article/detail?id=2
         * h5_about : http://www.eosdapp.co/article/detail?id=3
         * h5_notice_base_url : http://www.eosdapp.co/notice
         * h5_article_base_url : http://www.eosdapp.co/article
         */

        private String site_logo;
        private String foot_logo;
        private String site_title;
        private String copyright_info;
        private int app_ali_vcode_enabled;
        private VersionBean version;
        private AppDownUrlBean app_down_url;
        private String h5_agreement;
        private String h5_statement;
        private String h5_about;
        private String h5_notice_base_url;
        private String h5_article_base_url;

        public String getSite_logo()
        {
            return site_logo;
        }

        public void setSite_logo(String site_logo)
        {
            this.site_logo = site_logo;
        }

        public String getFoot_logo()
        {
            return foot_logo;
        }

        public void setFoot_logo(String foot_logo)
        {
            this.foot_logo = foot_logo;
        }

        public String getSite_title()
        {
            return site_title;
        }

        public void setSite_title(String site_title)
        {
            this.site_title = site_title;
        }

        public String getCopyright_info()
        {
            return copyright_info;
        }

        public void setCopyright_info(String copyright_info)
        {
            this.copyright_info = copyright_info;
        }

        public int getApp_ali_vcode_enabled()
        {
            return app_ali_vcode_enabled;
        }

        public void setApp_ali_vcode_enabled(int app_ali_vcode_enabled)
        {
            this.app_ali_vcode_enabled = app_ali_vcode_enabled;
        }

        public VersionBean getVersion()
        {
            return version;
        }

        public void setVersion(VersionBean version)
        {
            this.version = version;
        }

        public AppDownUrlBean getApp_down_url()
        {
            return app_down_url;
        }

        public void setApp_down_url(AppDownUrlBean app_down_url)
        {
            this.app_down_url = app_down_url;
        }

        public String getH5_agreement()
        {
            return h5_agreement;
        }

        public void setH5_agreement(String h5_agreement)
        {
            this.h5_agreement = h5_agreement;
        }

        public String getH5_statement()
        {
            return h5_statement;
        }

        public void setH5_statement(String h5_statement)
        {
            this.h5_statement = h5_statement;
        }

        public String getH5_about()
        {
            return h5_about;
        }

        public void setH5_about(String h5_about)
        {
            this.h5_about = h5_about;
        }

        public String getH5_notice_base_url()
        {
            return h5_notice_base_url;
        }

        public void setH5_notice_base_url(String h5_notice_base_url)
        {
            this.h5_notice_base_url = h5_notice_base_url;
        }

        public String getH5_article_base_url()
        {
            return h5_article_base_url;
        }

        public void setH5_article_base_url(String h5_article_base_url)
        {
            this.h5_article_base_url = h5_article_base_url;
        }

        public static class VersionBean
        {
            /**
             * has_upgrade : 0
             * forced_upgrade : 0
             */

            private int has_upgrade;
            private int forced_upgrade;

            public int getHas_upgrade()
            {
                return has_upgrade;
            }

            public void setHas_upgrade(int has_upgrade)
            {
                this.has_upgrade = has_upgrade;
            }

            public int getForced_upgrade()
            {
                return forced_upgrade;
            }

            public void setForced_upgrade(int forced_upgrade)
            {
                this.forced_upgrade = forced_upgrade;
            }
        }

        public static class AppDownUrlBean
        {
            /**
             * ios_down_url : http://www.eosdapp.co/appdown
             * android_down_url : http://www.eosdapp.co/appdown
             */

            private String ios_down_url;
            private String android_down_url;

            public String getIos_down_url()
            {
                return ios_down_url;
            }

            public void setIos_down_url(String ios_down_url)
            {
                this.ios_down_url = ios_down_url;
            }

            public String getAndroid_down_url()
            {
                return android_down_url;
            }

            public void setAndroid_down_url(String android_down_url)
            {
                this.android_down_url = android_down_url;
            }
        }
    }
}
