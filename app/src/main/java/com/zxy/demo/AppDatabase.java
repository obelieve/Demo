package com.zxy.demo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * Created by zxy on 2018/8/17 10:36.
 */
@Database(entities = {User.class, Book.class}, version =4)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract UserDao userDao();

    public abstract BookDao bookDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("delete from table_user where _id = 13");
        }
    };
    static final Migration MIGRATION_2_4 = new Migration(2,4)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("delete from table_user where _id = 14");
        }
    };

    static final Migration MIGRATION_1_3 = new Migration(1,3)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("delete from table_user where _id = 15");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3,4)
    {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database)
        {
            database.execSQL("delete from table_user where _id = 16");
        }
    };

}
