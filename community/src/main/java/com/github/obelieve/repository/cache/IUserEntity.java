package com.github.obelieve.repository.cache;

import android.app.Activity;
import android.content.Context;

import com.github.obelieve.login.entity.UserEntity;

/**
 * Created by Admin
 * on 2020/9/3
 */
public interface IUserEntity {

    void initUserEntity();

    UserEntity getUserEntity();

    void setUserEntity(String json, UserEntity userEntity, Context context);

    void refreshUserEntity(Activity activity);

    void refreshIMTokenFromUserEntity(String imToken);

    void refreshTokenFromUserEntity(String token);

    void clearUserEntity(Activity activity);
}
