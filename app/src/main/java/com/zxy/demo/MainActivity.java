package com.zxy.demo;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zxy.utility.LogUtil;

/**
 * Room
 * 1. Table 表
 *
 * @Entity 定义一个表(默认表名 类名称)
 * @PrimaryKey 指定主键
 * @ColumnInfo 指定一个列名(默认列名 域名称)
 * @Ignore 忽略某个域名值
 * @Index 创建索引
 * @ForeignKey 表外键关联 (支持级联删除)
 * @Embedded 内嵌对象：相当于属性合并了
 * <p>
 * 2.  Dao 数据库增删改查 编译时检查
 * @Dao
 * @Insert(onConflict = OnConflictStrategy.REPLACE) （支持插入冲突替换）
 * @Update 根据主键更新
 * @Delete 根据主键删除
 * @Query("Select * from table_user where _id =:id") (:id 表示接受的参数值)
 * @Query("Delete from Book where user_id=:user_id") 可以指定条件进行删除
 * <p>
 * 3.  migration 迁移数据库
 * 版本库变化时，需要构建迁移
 * fallbackToDestructiveMigration() 调用该方法时，版本库不一致时，直接重建
 * PS：ver 1->3  如果migration存在，MIGRATION_1_2,MIGRATION_2_3,MIGRATION_1_3, 直接使用MIGRATION_1_3进行构建
 */
public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "ok.db")
                        .addMigrations(
                                AppDatabase.MIGRATION_1_2,
                                AppDatabase.MIGRATION_2_4,
                                AppDatabase.MIGRATION_1_3,
                                AppDatabase.MIGRATION_3_4)
//                        .fallbackToDestructiveMigration()
                        .build();
                UserDao userDao = db.userDao();
//                for (int i = 0; i < 16; i++)
//                {
//                    User user = new User("xiao", 19);
//                    userDao.insertUsers(user);
//                }


//                BookDao bookDao = db.bookDao();
//                for (int i = 0; i < 4; i++)
//                {
//                    Book book = new Book();
//                    book.setName("book1");
//                    book.setType("1#");
//                    book.setUserId(i <= 2 ? 4 : 5);
//                    bookDao.insertBook(book);
//                }

//                User user = new User("", 0);
//                user.setId(4);
//                userDao.deleteUsers(user);
//                LogUtil.e("del:" + bookDao.deleteBook(4) + "   ==" + bookDao.loadAllBook() + "");

                LogUtil.e(" -%%%%%-" + userDao.loadAllUsers());
            }
        }).start();
    }
}
