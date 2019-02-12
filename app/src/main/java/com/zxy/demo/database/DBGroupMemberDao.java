package com.zxy.demo.database;

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
public interface DBGroupMemberDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupMember(DBGroupMemberModel... GroupMemberModels);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupMember(List<DBGroupMemberModel> GroupMemberModels);

    @Insert
    void insertGroupMember(DBGroupMemberModel GroupModel, DBGroupMemberModel GroupModel2);

    @Update
    int updateGroupMember(DBGroupMemberModel GroupModel);

    @Update
    void updateGroupMember(DBGroupMemberModel... GroupMemberModels);//可以使用int值返回值

    @Delete
    void deleteGroupMember(DBGroupMemberModel... GroupMemberModels);//根据Group主键删除 Group

    @Query("Delete from table_group_member where group_id=:group_id")
    int deleteGroupAllMember(String group_id);

    @Query("Delete from table_group_member where user_id=:user_id")
    int deleteGroupMember(String user_id);

    @Query("Select * from table_group_member where group_id=:group_id")
    DBGroupMemberModel[] loadAllGroupMember(String group_id);

    @Query("Select * from table_group_member where user_id=:user_id")
    DBGroupMemberModel[] loadGroupMember(String user_id);

    @Query("Select * from table_group_member where group_id=:group_id and user_id=:user_id")
    DBGroupMemberModel[] loadGroupMember(String group_id, String user_id);

    @Query("Select * from table_group_member ")
    List<DBGroupMemberModel> loadAllGroupMember();
    //多表查询 @Query
}
