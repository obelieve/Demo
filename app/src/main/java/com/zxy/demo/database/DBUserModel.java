package com.zxy.demo.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * 用户-表
 * Created by zxy on 2019/1/10 16:50.
 */
@Entity(tableName = "table_user", indices = {@Index(value = {"user_id", "certified_type"}, unique = true)})
public class DBUserModel
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private String id;
    @ColumnInfo(name = "nickname")
    private String nickname;
    @ColumnInfo(name = "avatar")
    private String avatar;
    @ColumnInfo(name = "certified_type")
    private int certified_type;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
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

    public int getCertified_type()
    {
        return certified_type;
    }

    public void setCertified_type(int certified_type)
    {
        this.certified_type = certified_type;
    }


    public static UserInfo getUserInfoInstance(DBUserModel model)
    {
        if (model == null)
            return null;
        Uri uri = null;
        if (model.getAvatar() != null)
        {
            uri = Uri.parse(model.getAvatar());
        }
        UserInfo userInfo = new UserInfo(model.getId(), model.getNickname(), uri);
        return userInfo;
    }

    @Override
    public String toString()
    {
        return "DBUserModel{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", certified_type=" + certified_type + "}'";
    }
}
