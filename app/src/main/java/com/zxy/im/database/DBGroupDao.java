package com.zxy.im.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * 群-操作
 * Created by zxy on 2018/8/20 14:47.
 */
@Dao
public interface DBGroupDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(DBGroupModel... GroupModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(List<DBGroupModel> groupModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroup(DBGroupModel GroupModel, DBGroupModel GroupModel2);

    @Update
    int updateGroup(DBGroupModel GroupModel);

    @Update
    void updateGroup(DBGroupModel... GroupModels);//可以使用int值返回值

    @Delete
    void deleteGroup(DBGroupModel... GroupModels);//根据Group主键删除 Group

    @Query("Delete from table_Group where group_id=:id")
    int deleteGroup(String id);

    @Query("Select * from table_Group where group_id=:id")
    DBGroupModel loadGroup(String id);

    @Query("Select * from table_Group where group_id in(:ids)")
    List<DBGroupModel> loadAllGroup(List<String> ids);

    @Query("Select * from table_Group ")
    List<DBGroupModel> loadAllGroup();
    //多表查询 @Query
}
