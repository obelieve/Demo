package com.zxy.im.dao;

import com.zxy.frame.cache.Dao;
import com.zxy.im.model.UserModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class UserModelDao
{
    public static UserInfo sUserInfo = null;

    public static void insertOrUpdate(UserModel userModel)
    {
        synchronized (UserModelDao.class)
        {
            if (sUserInfo == null)
            {
                sUserInfo = userModel.newUserInfo();
            } else
            {
                sUserInfo.setUserId(userModel.getId());
                sUserInfo.setName(userModel.getNickname());
                sUserInfo.setPortraitUri(userModel.getAvatarUri());
            }
            RongIM.getInstance().setCurrentUserInfo(sUserInfo);
        }

        Dao.insertOrUpdate(userModel,UserModel.class);
    }

    public static String getUserId()
    {
        final UserModel model = query();
        if (model == null)
            return null;
        return model.getId();
    }

    public static UserModel query()
    {
        return  Dao.query(UserModel.class);
    }

    public static void delete()
    {
        Dao.delete(UserModel.class);
    }
}
