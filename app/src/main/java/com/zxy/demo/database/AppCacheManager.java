package com.zxy.demo.database;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zxy.utility.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zxy on 2019/1/10 17:51.
 */

public class AppCacheManager
{
    public static final String TAG = AppCacheManager.class.getSimpleName();

    private static volatile AppCacheManager instance;
    private static Context mContext;

    private Handler mHandler;
    private ExecutorService mExecutorService;
    private AppDatabase mAppDatabase;

    Map<String, DBUserModel> mDBUserModelMap = new HashMap<>();
    Map<String, DBGroupModel> mDBGroupModelMap = new HashMap<>();

    Map<String, List<DBGroupMemberModel>> mDBGroupMemberGroupKeylMap = new HashMap<>();//每个群的用户id
    Map<String, DBGroupMemberModel> mDBGroupMemberUserKeyMap = new HashMap<>();//这个DBGroupMemberModel使用时，只取用户的基本数据(头像、认证类型等)

    private AppCacheManager(Context context)
    {
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        mExecutorService = Executors.newFixedThreadPool(5);
        mAppDatabase = AppDatabase.getInstance(context);
    }

    public static void init(Context context)
    {
        mContext = context;
    }

    public static AppCacheManager getInstance()
    {
        if (instance == null)
        {
            synchronized (AppCacheManager.class)
            {
                if (instance == null)
                {
                    instance = new AppCacheManager(mContext);
                }
            }
        }
        return instance;
    }

    /**
     * Test
     * @param activity
     */
    private static void testRx(Activity activity)
    {
        AppCacheManager.init(activity.getApplicationContext());
        AppCacheManager.getInstance().getUserInfo("1");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                UserInfo userInfo = AppCacheManager.getInstance().getUserInfo("1");
                LogUtil.e(userInfo + "");
            }
        }, 1000);
    }

    public void executeThread(Runnable runnable)
    {
        mExecutorService.execute(runnable);
    }

    public UserInfo getUserInfo(final String user_id)
    {
        UserInfo info = getMemoryUserInfo(user_id);
        if (info != null)
        {
            return info;
        }
        cacheToMemoryDBUserModel(user_id);
        return null;
    }

    /**
     * 从数据库/网络获取DBUserModel 存储到内存中
     *
     * @param user_id
     */
    private void cacheToMemoryDBUserModel(final String user_id)
    {
        Observable.create(new ObservableOnSubscribe<DBUserModel>()
        {
            @Override
            public void subscribe(ObservableEmitter<DBUserModel> emitter)
            {
                DBUserModel resultDBUserModel = null;
                resultDBUserModel = getSQLDBUserModel(user_id);
                if (resultDBUserModel == null)
                {
                    resultDBUserModel = getSyncNetDBUserModel(user_id);
                }
                if (resultDBUserModel != null)
                {
                    emitter.onNext(resultDBUserModel);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DBUserModel>()
                {
                    @Override
                    public void accept(DBUserModel userModel)
                    {
                        mDBUserModelMap.put(userModel.getId(), userModel);
                    }
                });
    }

    /**
     * 从数据库/网络获取DBUserModel 存储到内存中
     *
     * @param group_id
     */
    private void cacheToMemoryDBGroupModel(final String group_id)
    {
        Observable.create(new ObservableOnSubscribe<DBGroupModel>()
        {
            @Override
            public void subscribe(ObservableEmitter<DBGroupModel> emitter)
            {
                DBGroupModel resultDBGroupModel = null;
                resultDBGroupModel = getSQLDBGroupModel(group_id);
                if (resultDBGroupModel == null)
                {
                    resultDBGroupModel = getSyncNetDBGroupModel(group_id);
                }
                if (resultDBGroupModel != null)
                {
                    emitter.onNext(resultDBGroupModel);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DBGroupModel>()
                {
                    @Override
                    public void accept(DBGroupModel groupModel)
                    {
                        mDBGroupModelMap.put(groupModel.getId(), groupModel);
                    }
                });
    }

    /**
     * 获取内存用户数据
     *
     * @param user_id
     * @return
     */
    private UserInfo getMemoryUserInfo(String user_id)
    {
        UserInfo userInfo = null;
        if (mDBUserModelMap.containsKey(user_id))//朋友列表数据缓存
        {
            userInfo = DBUserModel.getUserInfoInstance(mDBUserModelMap.get(user_id));
        } else if (mDBGroupMemberUserKeyMap.containsKey(user_id))//群成员列表数据缓存
        {
            userInfo = DBGroupMemberModel.getUserInfoInstance(mDBGroupMemberUserKeyMap.get(user_id));
        }
        LogUtil.e(userInfo + "");
        return userInfo;
    }

    /**
     * 获取内存DBUserModel数据
     *
     * @param user_id
     * @return
     */
    public DBUserModel getMemoryDBUserModel(String user_id)
    {
        if (mDBUserModelMap.containsKey(user_id))//朋友列表数据缓存
        {
            return mDBUserModelMap.get(user_id);
        } else if (mDBGroupMemberUserKeyMap.containsKey(user_id))//群成员列表数据缓存
        {
            return DBGroupMemberModel.getDBUserModelInstance(mDBGroupMemberUserKeyMap.get(user_id));
        }
        return null;
    }

    /**
     * 获取内存DBGroupModel数据
     *
     * @param group_id
     * @return
     */
    public DBGroupModel getMemoryDBGroupModel(String group_id)
    {
        if (mDBGroupModelMap.containsKey(group_id))
        {
            return mDBGroupModelMap.get(group_id);
        }
        return null;
    }

    /**
     * 获取数据库的用户数据
     *
     * @param user_id
     * @return
     */
    private DBUserModel getSQLDBUserModel(String user_id)
    {

        DBUserModel resultDBUserModel = null;
        DBUserModel userModel = mAppDatabase.userDao().loadUser(user_id);
        if (userModel != null)
        {
            resultDBUserModel = userModel;
        } else
        {
            DBGroupMemberModel[] groupMemberModels = mAppDatabase.groupMemberDao().loadGroupMember(user_id);
            if (groupMemberModels != null && groupMemberModels.length > 0)
            {
                resultDBUserModel = DBGroupMemberModel.getDBUserModelInstance(groupMemberModels[0]);
            }
        }
        LogUtil.e(resultDBUserModel + "");
        return resultDBUserModel;
    }

    /**
     * 获取数据库中群数据
     *
     * @param group_id
     * @return
     */
    private DBGroupModel getSQLDBGroupModel(String group_id)
    {
        DBGroupModel dbGroupModel = mAppDatabase.groupDao().loadGroup(group_id);
        if (dbGroupModel != null)
        {
            return dbGroupModel;
        }
        return null;
    }

    /**
     * 同步-获取网络中的用户数据
     *
     * @param user_id
     * @return
     */
    private DBUserModel getSyncNetDBUserModel(String user_id)
    {

        DBUserModel model = null;

        try
        {
            DBUserModel dbUserModel = new DBUserModel();
            dbUserModel.setId(user_id);
            dbUserModel.setCertified_type(1);
            dbUserModel.setAvatar("http://www.im.fanwe.cn/static/asset/noavatar.gif");
            dbUserModel.setNickname("No." + user_id);
            mAppDatabase.userDao().insertUser(dbUserModel);
            model = dbUserModel;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        LogUtil.e(model + "");
        return model;
    }

    /**
     * 同步-获取网络中的群数据
     *
     * @param group_id
     * @return
     */
    private DBGroupModel getSyncNetDBGroupModel(String group_id)
    {
        DBGroupModel model = null;

        try
        {
            GroupModel groupModel = new GroupModel();
            DBGroupModel dbGroupModel = DBGroupModel.getInstance(groupModel);
            mAppDatabase.groupDao().insertGroup(dbGroupModel);
            model = dbGroupModel;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 更新群成员-信息
     *
     * @param memberModels
     */
    public void updateDBGroupMemberModel(final List<DBGroupMemberModel> memberModels)
    {
        if (memberModels == null || memberModels.size() == 0)
        {
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<DBGroupMemberModel>>()
        {
            @Override
            public void subscribe(ObservableEmitter<List<DBGroupMemberModel>> emitter)
            {
                mAppDatabase.groupMemberDao().insertGroupMember(memberModels);
                emitter.onNext(memberModels);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DBGroupMemberModel>>()
                {
                    @Override
                    public void accept(List<DBGroupMemberModel> models) throws Exception
                    {
                        mDBGroupMemberGroupKeylMap.put(models.get(0).getGroup_id(), models);
                        for (DBGroupMemberModel model : models)
                        {
                            mDBGroupMemberUserKeyMap.put(model.getUser_id(), model);
                        }
                    }
                });
    }


    /**
     * 更新用户信息
     *
     * @param dbUserModel
     */
    public void updateDBUserModel(final DBUserModel dbUserModel)
    {
        if (dbUserModel == null)
            return;
        List<DBUserModel> list = new ArrayList<DBUserModel>();
        list.add(dbUserModel);
        updateDBUserModel(list);
    }

    /**
     * 更新用户信息
     *
     * @param userModels
     */
    public void updateDBUserModel(final List<DBUserModel> userModels)
    {
        if (userModels == null || userModels.size() == 0)
        {
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<DBUserModel>>()
        {
            @Override
            public void subscribe(ObservableEmitter<List<DBUserModel>> emitter)
            {
                mAppDatabase.userDao().insertUser(userModels);
                emitter.onNext(userModels);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DBUserModel>>()
                {
                    @Override
                    public void accept(List<DBUserModel> models) throws Exception
                    {
                        for (DBUserModel model : models)
                        {
                            mDBUserModelMap.put(model.getId(), model);
                        }
                    }
                });
    }

    /**
     * 更新群信息
     *
     * @param dbGroupModel
     */
    public void updateDBGroupModel(final DBGroupModel dbGroupModel)
    {
        if (dbGroupModel == null)
            return;
        List<DBGroupModel> list = new ArrayList<DBGroupModel>();
        list.add(dbGroupModel);
        updateDBGroupModel(list);
    }

    /**
     * 更新群信息
     *
     * @param dbGroupModels
     */
    public void updateDBGroupModel(final List<DBGroupModel> dbGroupModels)
    {
        if (dbGroupModels == null || dbGroupModels.size() == 0)
        {
            return;
        }
        Observable.create(new ObservableOnSubscribe<List<DBGroupModel>>()
        {
            @Override
            public void subscribe(ObservableEmitter<List<DBGroupModel>> emitter)
            {
                mAppDatabase.groupDao().insertGroup(dbGroupModels);
                emitter.onNext(dbGroupModels);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DBGroupModel>>()
                {
                    @Override
                    public void accept(List<DBGroupModel> models) throws Exception
                    {
                        for (DBGroupModel model : models)
                        {
                            mDBGroupModelMap.put(model.getId(), model);
                        }
                    }
                });
    }

}
