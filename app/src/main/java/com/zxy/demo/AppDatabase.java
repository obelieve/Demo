package com.zxy.demo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by zxy on 2018/8/17 10:36.
 */
@Database(entities = {User.class, Book.class}, version =1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract UserDao userDao();
}
