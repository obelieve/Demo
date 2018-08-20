package com.zxy.demo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by zxy on 2018/8/17 09:25.
 */
@Entity(tableName = "table_user",indices = {@Index(value = {"_id"},unique = true)})
public class User
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "age")
    private int age;
    @Ignore
    private Bitmap bitmap;
    //内嵌对象 相当于增加属性 street,state,city（如果有重名属性，可以设置prefix前缀）
    @Embedded
    private Address address;


    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

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

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", bitmap=" + bitmap +
                ", address=" + address +
                '}';
    }
}
