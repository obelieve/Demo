package com.zxy.im.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by zxy on 2019/1/11 16:00.
 */
@Entity(tableName = "table_group_member", primaryKeys = {"group_id", "user_id"},
        indices = {@Index(value = {"user_id"}, unique = true)})

public class DBGroupMemberModel
{
    @NonNull
    @ColumnInfo(name = "group_id")
    private String group_id;
    @NonNull
    @ColumnInfo(name = "user_id")
    private String user_id;
    @ColumnInfo(name = "mobile")
    private String mobile;//手机号
    @ColumnInfo(name = "nickname")
    private String nickname;//用户昵称
    @ColumnInfo(name = "avatar")
    private String avatar;//用户头像
    @ColumnInfo(name = "certified_type")
    private int certified_type;//'认证类型 0-个人认证 1-机构认证, -1未认证',

    @ColumnInfo(name = "is_online")
    private int is_online;//是否登录 0-未登录，1-已登录
    @ColumnInfo(name = "last_login_time")
    private String last_login_time;//最近登录时间
    @ColumnInfo(name = "identity")
    private int identity;//0-普通群员 1-管理员 2-群主
    @ColumnInfo(name = "group_nickname")
    private String group_nickname;//群昵称
    @ColumnInfo(name = "is_friend")
    private int is_friend; //是否为好友 0-不是 1-是
    @ColumnInfo(name = "is_black")
    private int is_black; //是否黑名单  0-不是 1-是
    @ColumnInfo(name = "remark_name")
    private String remark_name; //昵称     显示昵称(为好友时显示备注)

    @NonNull
    public String getGroup_id()
    {
        return group_id;
    }

    public void setGroup_id(@NonNull String group_id)
    {
        this.group_id = group_id;
    }

    @NonNull
    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(@NonNull String user_id)
    {
        this.user_id = user_id;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public int getIs_online()
    {
        return is_online;
    }

    public void setIs_online(int is_online)
    {
        this.is_online = is_online;
    }

    public String getLast_login_time()
    {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time)
    {
        this.last_login_time = last_login_time;
    }

    public int getIdentity()
    {
        return identity;
    }

    public void setIdentity(int identity)
    {
        this.identity = identity;
    }

    public String getGroup_nickname()
    {
        return group_nickname;
    }

    public void setGroup_nickname(String group_nickname)
    {
        this.group_nickname = group_nickname;
    }

    public int getIs_friend()
    {
        return is_friend;
    }

    public void setIs_friend(int is_friend)
    {
        this.is_friend = is_friend;
    }

    public int getIs_black()
    {
        return is_black;
    }

    public void setIs_black(int is_black)
    {
        this.is_black = is_black;
    }

    public String getRemark_name()
    {
        return remark_name;
    }

    public void setRemark_name(String remark_name)
    {
        this.remark_name = remark_name;
    }

    public int getCertified_type()
    {
        return certified_type;
    }

    public void setCertified_type(int certified_type)
    {
        this.certified_type = certified_type;
    }


    public static DBUserModel getDBUserModelInstance(DBGroupMemberModel model)
    {
        if (model == null)
            return null;
        DBUserModel userModel = new DBUserModel();
        userModel.setId(model.getUser_id());
        userModel.setAvatar(model.getAvatar());
        userModel.setNickname(model.getNickname());
        userModel.setCertified_type(model.getCertified_type());
        return userModel;
    }

    public static UserInfo getUserInfoInstance(DBGroupMemberModel model)
    {
        if (model == null)
            return null;
        Uri uri = null;
        if (model.getAvatar() != null)
        {
            uri = Uri.parse(model.getAvatar());
        }
        UserInfo userInfo = new UserInfo(model.getUser_id(), model.getNickname(), uri);
        return userInfo;
    }

    @Override
    public String toString()
    {
        return "DBGroupMemberModel{" +
                "group_id='" + group_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", is_online=" + is_online +
                ", last_login_time='" + last_login_time + '\'' +
                ", identity=" + identity +
                ", group_nickname='" + group_nickname + '\'' +
                ", is_friend=" + is_friend +
                ", is_black=" + is_black +
                ", remark_name='" + remark_name + '\'' +
                ", certified_type=" + certified_type +
                '}';
    }
}
