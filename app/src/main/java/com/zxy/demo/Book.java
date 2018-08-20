package com.zxy.demo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by zxy on 2018/8/17 09:42.
 */
//外键 user_id 默认支持级联删除和更新
@Entity(foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "_id",childColumns = "user_id")},indices = @Index("user_id"))
public class Book
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String type;

    @ColumnInfo(name = "user_id")
    private int userId;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                '}';
    }
}
