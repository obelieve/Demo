package com.github.obelieve.community.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.App;
import com.github.obelieve.community.bean.BBSUserInfoEntity;
import com.github.obelieve.community.bean.BBSUserTrendsEntity;
import com.github.obelieve.community.bean.BBSUserZanEntity;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiServiceException;


public class PersonalPageViewModel extends ViewModel {


    //个人主页信息数据
    private MutableLiveData<BBSUserInfoEntity> bbsUserInfoEntityMutableLiveData = new MutableLiveData<>();

    //动态列表数据
    private MutableLiveData<BBSUserTrendsEntity> bbsUserTrendsEntityMutableLiveData = new MutableLiveData<>();

    //点赞列表数据
    private MutableLiveData<BBSUserZanEntity> bbsUserZanEntityMutableLiveData = new MutableLiveData<>();

    /**
     * true 加载更多，false 加载完成
     */
    private MutableLiveData<Boolean> mLoadMoreMutableLiveData = new MutableLiveData<>();

    private int page = 1;
    private boolean mLoadMore;

    /**
     * 获取个人主页信息
     *
     * @param userId
     */
    public void getUserInfo(Activity activity, int userId) {
        ApiServiceWrapper.wrap(App.getServiceInterface().bbsUserInfo(userId),BBSUserInfoEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<BBSUserInfoEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        Log.e("RespondThrowable", e.getMessage());
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<BBSUserInfoEntity> response, boolean isProcessed) {
                        bbsUserInfoEntityMutableLiveData.setValue(response.getEntity());
                    }
                });
    }

    /**
     * 个人主页动态列表
     *
     * @param userId 用户id
     * @param isMore 是否加载更多
     */
    public void getTrends(Activity activity, int userId, boolean isMore) {
        mLoadMore = isMore;
        if (!isMore) {
            page = 1;
        }

        ApiServiceWrapper.bbsUserTrends(activity,userId, page, new ApiBaseSubscribe<ApiBaseResponse<BBSUserTrendsEntity>>() {
            @Override
            public void onError(ApiServiceException e) {
                Log.e("RespondThrowable", e.getMessage());
                if(isMore){
                    mLoadMoreMutableLiveData.postValue(true);
                }else{
                    mLoadMoreMutableLiveData.postValue(false);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<BBSUserTrendsEntity> response, boolean isProcessed) {
                if (response.getEntity().getHas_next_page() == 1) {
                    page = response.getEntity().getCurrent_page() + 1;
                }
                bbsUserTrendsEntityMutableLiveData.postValue(response.getEntity());
                if(isMore){
                    mLoadMoreMutableLiveData.postValue(true);
                }else{
                    mLoadMoreMutableLiveData.postValue(false);
                }
            }
        });
    }

    /**
     * 个人主页点赞列表
     *
     * @param userId 用户id
     * @param isMore 是否加载更多
     */
    public void getZan(Activity activity, int userId, boolean isMore) {
        mLoadMore = isMore;
        if (!isMore) {
            page = 1;
        }
        ApiServiceWrapper.bbsUserZan(activity, userId, page, new ApiBaseSubscribe<ApiBaseResponse<BBSUserZanEntity>>() {
            @Override
            public void onError(ApiServiceException e) {
                Log.e("RespondThrowable", e.getMessage());
                if(isMore){
                    mLoadMoreMutableLiveData.postValue(true);
                }else{
                    mLoadMoreMutableLiveData.postValue(false);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<BBSUserZanEntity> response, boolean isProcessed) {
                if (response.getEntity().getHas_next_page() == 1) {
                    page = response.getEntity().getCurrent_page() + 1;
                }
                bbsUserZanEntityMutableLiveData.postValue(response.getEntity());
                if(isMore){
                    mLoadMoreMutableLiveData.postValue(true);
                }else{
                    mLoadMoreMutableLiveData.postValue(false);
                }
            }
        });
    }




    public MutableLiveData<BBSUserInfoEntity> getBbsUserInfoEntityMutableLiveData() {
        return bbsUserInfoEntityMutableLiveData;
    }

    public MutableLiveData<BBSUserTrendsEntity> getBbsUserTrendsEntityMutableLiveData() {
        return bbsUserTrendsEntityMutableLiveData;
    }

    public MutableLiveData<BBSUserZanEntity> getBbsUserZanEntityMutableLiveData() {
        return bbsUserZanEntityMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoadMoreMutableLiveData() {
        return mLoadMoreMutableLiveData;
    }

    public boolean isLoadMore() {
        return mLoadMore;
    }
}
