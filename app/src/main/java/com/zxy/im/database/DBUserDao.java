package com.zxy.im.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * 用户-操作
 * Created by zxy on 2018/8/20 14:47.
 */
@Dao
public interface DBUserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(List<DBUserModel> userModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(DBUserModel... userModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(DBUserModel userModel, DBUserModel userModel2);

    @Update
    int updateUser(DBUserModel userModel);

    @Update
    void updateUser(DBUserModel... userModels);//可以使用int值返回值

    @Delete
    void deleteUser(DBUserModel... userModels);//根据User主键删除 User

    @Query("Delete from table_user where user_id=:id")
    int deleteUser(String id);

    @Query("Select * from table_user where user_id=:id")
    DBUserModel loadUser(String id);

    @Query("Select * from table_user where user_id in (:ids)")
    List<DBUserModel> loadAllUser(List<String> ids);

    @Query("Select * from table_user ")
    List<DBUserModel> loadAllUser();
    //多表查询 @Query
}
