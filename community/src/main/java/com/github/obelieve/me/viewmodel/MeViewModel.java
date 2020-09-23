package com.github.obelieve.me.viewmodel;

import android.app.Activity;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.App;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.main.UserNavInfoEntity;
import com.github.obelieve.me.bean.CenterMenuEntity;
import com.github.obelieve.repository.cache.PreferenceUtil;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.net.gson.MGson;

public class MeViewModel extends ViewModel {

    private MutableLiveData<Boolean> mShowProgressMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mErrorMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<UserNavInfoEntity> mUserNavInfoEntityMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CenterMenuEntity> mCenterMenuEntityMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<UserEntity> mUserEntityMutableLiveData = new MutableLiveData<>();

    boolean mFirstLoadRequestData = true;

    /**
     * 请求数据
     */
    public void requestData(Activity activity) {
        if (mFirstLoadRequestData) {
            mFirstLoadRequestData = false;
            UserNavInfoEntity userNavInfoEntity = getCacheUserNavInfoEntity();
            if (userNavInfoEntity != null) {
                mUserNavInfoEntityMutableLiveData.setValue(userNavInfoEntity);
            }
            CenterMenuEntity centerMenuEntity = getCacheCenterMenuEntity();
            if (centerMenuEntity != null) {
                mCenterMenuEntityMutableLiveData.setValue(centerMenuEntity);
            }
            centerMenu(activity,centerMenuEntity == null);
        } else {
            centerMenu(activity,false);
        }
        if (!TextUtils.isEmpty(SystemValue.token)) {
            userNavInfo(activity,true);
        }
    }

    /**
     * 用户信息
     *
     * @param notify
     */
    public void userNavInfo(Activity activity,boolean notify) {
        ApiService.wrap(App.getServiceInterface().userNavInfo(), UserNavInfoEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserNavInfoEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {

                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<UserNavInfoEntity> response, boolean isProcessed) {
                        PreferenceUtil.putString(App.getContext(), PreferenceConst.SP_CJ_USER_NAV_INFO_CACHE, response.getData());
                        if (notify) {
                            mUserNavInfoEntityMutableLiveData.setValue(response.getEntity());
                        }
                    }
                });
    }

    /**
     * 菜单导航
     *
     * @param progress
     */
    public void centerMenu(Activity activity,boolean progress) {
        if (progress) {
            mShowProgressMutableLiveData.setValue(true);
        }
        ApiService.wrap(App.getServiceInterface().centerMenuU(),CenterMenuEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<CenterMenuEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                if (progress) {
                    mShowProgressMutableLiveData.setValue(false);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<CenterMenuEntity> response, boolean isProcessed) {
                PreferenceUtil.putString(App.getContext(), PreferenceConst.SP_CJ_CENTER_MENU_CACHE, response.getData());
                if (progress) {
                    mShowProgressMutableLiveData.setValue(false);
                }
                CenterMenuEntity entity = response.getEntity();
                mCenterMenuEntityMutableLiveData.setValue(entity);
            }
        });
    }

    private CenterMenuEntity getCacheCenterMenuEntity() {
        String centerMenu = PreferenceUtil.getString(App.getContext(), PreferenceConst.SP_CJ_CENTER_MENU_CACHE);
        CenterMenuEntity centerMenuEntity = null;
        if (!TextUtils.isEmpty(centerMenu)) {
            try {
                centerMenuEntity = MGson.newGson().fromJson(centerMenu, CenterMenuEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (centerMenuEntity != null) {
            mCenterMenuEntityMutableLiveData.setValue(centerMenuEntity);
        }
        return centerMenuEntity;
    }

    private UserNavInfoEntity getCacheUserNavInfoEntity() {
        UserNavInfoEntity userNavInfoEntity = null;
        String userNavInfo = PreferenceUtil.getString(App.getContext(), PreferenceConst.SP_CJ_USER_NAV_INFO_CACHE);
        if (!TextUtils.isEmpty(userNavInfo)) {
            try {
                userNavInfoEntity = MGson.newGson().fromJson(userNavInfo, UserNavInfoEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userNavInfoEntity;
    }

    public MutableLiveData<Boolean> getShowProgressMutableLiveData() {
        return mShowProgressMutableLiveData;
    }

    public MutableLiveData<UserNavInfoEntity> getUserNavInfoEntityMutableLiveData() {
        return mUserNavInfoEntityMutableLiveData;
    }

    public MutableLiveData<CenterMenuEntity> getCenterMenuEntityMutableLiveData() {
        return mCenterMenuEntityMutableLiveData;
    }

    public MutableLiveData<String> getErrorMutableLiveData() {
        return mErrorMutableLiveData;
    }

    public MutableLiveData<UserEntity> getUserEntityMutableLiveData() {
        return mUserEntityMutableLiveData;
    }
}
