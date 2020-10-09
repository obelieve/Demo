package com.zxy.demo.entity;

import com.zxy.frame.utils.proguard.UnProguard;

import java.io.Serializable;

public class VersionUpdateEntity implements Serializable, UnProguard {

    /**
     * remark : v1.6.1
     * is_need : 1
     * version_new : 1.6.1
     * size : 5MB
     * content : 更新内容：
     1、更新球队数据展示；
     2、修复已知Bug；
     3、用户体验优化。
     * download_url : http://image.2048.com/2048_V1.0.2_2048_android_2.apk
     * enforce : 0
     */

    private String remark;
    private int is_need;
    private String version_new;
    private String size;
    private String content;
    private String download_url;
    private int enforce;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIs_need() {
        return is_need;
    }

    public void setIs_need(int is_need) {
        this.is_need = is_need;
    }

    public String getVersion_new() {
        return version_new;
    }

    public void setVersion_new(String version_new) {
        this.version_new = version_new;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public int getEnforce() {
        return enforce;
    }

    public void setEnforce(int enforce) {
        this.enforce = enforce;
    }
}
