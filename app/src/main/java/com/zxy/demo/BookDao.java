package com.zxy.demo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by zxy on 2018/8/20 14:47.
 */
@Dao
public interface BookDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBook(Book... books);

    @Insert
    void insertBook(Book book1, Book book2);

    @Insert
    void insertBookAndFriends(Book user, List<Book> friends);

    @Update
    int updateBook(Book user);

    @Update
    void updateBook(Book...books);//可以使用int值返回值

    @Delete
    void deleteBook(Book...books);//根据Book主键删除 Book

    @Query("Delete from Book where user_id=:user_id")
    int deleteBook(int user_id);

    @Query("Select * from Book where id =:id")//@Query是编译时处理，语句出错编译时会提示
    Book[] loadAllBook(int id);

    @Query("Select * from Book ")
    List<Book> loadAllBook();
    //多表查询 @Query
}
