package com.zxy.im.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by zxy on 2018/8/17 10:36.
 */
@Database(entities = {DBUserModel.class, DBGroupModel.class,DBGroupMemberModel.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DB_NAME = "im.db";

    public abstract DBUserDao userDao();

    public abstract DBGroupDao groupDao();

    public abstract DBGroupMemberDao groupMemberDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            synchronized (AppDatabase.class)
            {
                if (instance == null)
                {
                    instance = create(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static AppDatabase create(final Context context)
    {

        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).build();
    }
}
