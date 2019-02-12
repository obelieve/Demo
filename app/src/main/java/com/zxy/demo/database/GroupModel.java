package com.zxy.demo.database;

import java.io.Serializable;

public class GroupModel implements Serializable
{
    private String id;
    private String owner_id;
    private int level;//等级，与最大人数挂钩
    private String add_order;
    private String name;
    private String avatar;
    private String summary;
    private String notice;
    private String create_time;
    private int pid;//父群ID
    private int member_number;//当前群全部成员人数
    private int max_number;//当前群最大人数
    private String updated_at;
    private int status;
    private int add_check;//加群验证 0-关闭 1-开启
    private int private_chat;//禁止私聊 0-关闭 1-开启
    private int image_send;//禁止群聊发图 0-关闭 1-开启
    private int link_send;//禁止群聊发链接 0-关闭 1-开启
    private int screen_shot;//群聊天截图提示 0-关闭 1-开启
    private int add_son_group;//创建子群 0-允许创建，1-需要管理员同意，2-禁止创建子群
    private int manager_number;//当前管理员人数
    private int is_push;//屏蔽提示 0-关闭 1-开启
    private int is_search;//是否能被搜索 0-能 1-不能
    private int identity;//请求人在群中身份 0-群员 1-管理 2-群主 3-不在群中
    private int is_certified; //'认证 0未认证  1-通过；2-拒绝；3-撤销  -1待审核',
    private int certified_type;//'认证类型 0-个人认证 1-机构认证, -1未认证',
    private String certified_info;//'认证说明',

    private String enter_url;//群链接地址

    public String getEnter_url()
    {
        return enter_url;
    }

    public void setEnter_url(String enter_url)
    {
        this.enter_url = enter_url;
    }

    public int getIs_certified()
    {
        return is_certified;
    }

    public void setIs_certified(int is_certified)
    {
        this.is_certified = is_certified;
    }

    public int getCertified_type()
    {
        return certified_type;
    }

    public void setCertified_type(int certified_type)
    {
        this.certified_type = certified_type;
    }

    public String getCertified_info()
    {
        return certified_info;
    }

    public void setCertified_info(String certified_info)
    {
        this.certified_info = certified_info;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOwner_id()
    {
        return owner_id;
    }

    public void setOwner_id(String owner_id)
    {
        this.owner_id = owner_id;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getAdd_order()
    {
        return add_order;
    }

    public void setAdd_order(String add_order)
    {
        this.add_order = add_order;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getNotice()
    {
        return notice;
    }

    public void setNotice(String notice)
    {
        this.notice = notice;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public int getPid()
    {
        return pid;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
    }

    public int getMember_number()
    {
        return member_number;
    }

    public void setMember_number(int member_number)
    {
        this.member_number = member_number;
    }

    public int getMax_number()
    {
        return max_number;
    }

    public void setMax_number(int max_number)
    {
        this.max_number = max_number;
    }

    public String getUpdated_at()
    {
        return updated_at;
    }

    public void setUpdated_at(String updated_at)
    {
        this.updated_at = updated_at;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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

    public int getAdd_son_group()
    {
        return add_son_group;
    }

    public void setAdd_son_group(int add_son_group)
    {
        this.add_son_group = add_son_group;
    }

    public int getManager_number()
    {
        return manager_number;
    }

    public void setManager_number(int manager_number)
    {
        this.manager_number = manager_number;
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

    public int getIdentity()
    {
        return identity;
    }

    public void setIdentity(int identity)
    {
        this.identity = identity;
    }
}
