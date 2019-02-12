package com.zxy.im.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 群-表
 * Created by zxy on 2019/1/10 16:50.
 */
@Entity(tableName = "table_group", indices = {@Index(value = {"group_id", "certified_type"}, unique = true)})
public class DBGroupModel
{
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "group_id")
    private String id;
    @ColumnInfo(name = "nickname")
    private String nickname;
    @ColumnInfo(name = "avatar")
    private String avatar;
    @ColumnInfo(name = "certified_type")
    private int certified_type;
    @ColumnInfo(name = "save_last_time")
    private long save_last_time;
    @ColumnInfo(name = "identity")
    private int identity;//请求人在群中身份 0-群员 1-管理 2-群主 3-不在群中

    @ColumnInfo(name = "add_check")
    private int add_check;//加群验证 0-关闭 1-开启
    @ColumnInfo(name = "private_chat")
    private int private_chat;//禁止私聊 0-关闭 1-开启
    @ColumnInfo(name = "image_send")
    private int image_send;//禁止群聊发图 0-关闭 1-开启
    @ColumnInfo(name = "link_send")
    private int link_send;//禁止群聊发链接 0-关闭 1-开启
    @ColumnInfo(name = "screen_shot")
    private int screen_shot;//群聊天截图提示 0-关闭 1-开启
    @ColumnInfo(name = "is_push")
    private int is_push;//屏蔽提示 0-关闭 1-开启
    @ColumnInfo(name = "is_search")
    private int is_search;//是否能被搜索 0-能 1-不能

    public int getIdentity()
    {
        return identity;
    }

    public void setIdentity(int identity)
    {
        this.identity = identity;
    }

    public int getIs_push()
    {
        return is_push;
    }

    public void setIs_push(int is_push)
    {
        this.is_push = is_push;
    }

    public int getIs_search()
    {
        return is_search;
    }

    public void setIs_search(int is_search)
    {
        this.is_search = is_search;
    }

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

    public long getSave_last_time()
    {
        return save_last_time;
    }

    public void setSave_last_time(long save_last_time)
    {
        this.save_last_time = save_last_time;
    }

    public int getAdd_check()
    {
        return add_check;
    }

    public void setAdd_check(int add_check)
    {
        this.add_check = add_check;
    }

    public int getPrivate_chat()
    {
        return private_chat;
    }

    public void setPrivate_chat(int private_chat)
    {
        this.private_chat = private_chat;
    }

    public int getImage_send()
    {
        return image_send;
    }

    public void setImage_send(int image_send)
    {
        this.image_send = image_send;
    }

    public int getLink_send()
    {
        return link_send;
    }

    public void setLink_send(int link_send)
    {
        this.link_send = link_send;
    }

    public int getScreen_shot()
    {
        return screen_shot;
    }

    public void setScreen_shot(int screen_shot)
    {
        this.screen_shot = screen_shot;
    }


    public static List<DBGroupModel> getListInstance(List<GroupModel> groupModels)
    {
        if (groupModels != null && groupModels.size() > 0)
        {
            List<DBGroupModel> list = new ArrayList<>();
            for (GroupModel model : groupModels)
            {
                DBGroupModel dbGroupModel = getInstance(model);
                if (dbGroupModel != null)
                {
                    list.add(dbGroupModel);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public static DBGroupModel getInstance(GroupModel groupModel)
    {
        DBGroupModel model = new DBGroupModel();
        model.setId(groupModel.getId());
        model.setAvatar(groupModel.getAvatar());
        model.setNickname(groupModel.getName());
        model.setCertified_type(groupModel.getCertified_type());
        model.setAdd_check(groupModel.getAdd_check());
        model.setPrivate_chat(groupModel.getPrivate_chat());
        model.setImage_send(groupModel.getImage_send());
        model.setLink_send(groupModel.getLink_send());
        model.setScreen_shot(groupModel.getScreen_shot());
        model.setIs_push(groupModel.getIs_push());
        model.setIs_search(groupModel.getIs_search());
        return model;
    }

    @Override
    public String toString()
    {
        return "DBGroupModel{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", certified_type=" + certified_type +
                ", save_last_time=" + save_last_time +
                ", add_check=" + add_check +
                ", private_chat=" + private_chat +
                ", image_send=" + image_send +
                ", link_send=" + link_send +
                ", screen_shot=" + screen_shot +
                '}';
    }
}
