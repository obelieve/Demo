package com.zxy.demo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by zxy on 2018/8/17 09:50.
 */
@Dao
public interface UserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(User... users);

    @Insert
    void insertUsers(User user1, User user2);

    @Insert
    void insertUsersAndFriends(User user, List<User> friends);

    @Update
    void updateUsers(User...users);//可以使用int值返回值

    @Delete
    void deleteUsers(User...users);//根据User主键删除 User

    @Query("Select * from table_user where name =:name1")//@Query是编译时处理，语句出错编译时会提示
    User[] loadAllUsers(String name);

    @Query("Select name,age from table_user")//返回某些属性，User的子集
    List<UserNameTuple> loadSimpleUserInfo();

    //多表查询 @Query
}
