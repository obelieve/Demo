package com.github.obelieve.repository.cache;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.github.obelieve.App;
import com.github.obelieve.event.login.LogoutNotifyEvent;
import com.github.obelieve.event.login.UserInfoRefreshNotifyEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.net.UrlConst;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.google.gson.Gson;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.net.gson.MGson;
import com.zxy.frame.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class UserHelper {

    static volatile UserHelper instance;

    UserEntity mUserEntity;

    public static UserHelper getInstance() {
        if (instance == null) {
            synchronized (UserHelper.class) {
                if (instance == null) {
                    instance = new UserHelper();
                }
            }
        }
        return instance;
    }


    public UserEntity getUserEntity() {
        return mUserEntity;
    }

    public void setUserEntity(String json, UserEntity mUserEntity, Context context) {
        this.mUserEntity = mUserEntity;
        SystemValue.token = mUserEntity.token;
        SystemValue.imid = mUserEntity.imid;
        SystemValue.imtoken = mUserEntity.imtoken;
        syncCookie();
        //todo IM登录操作
//        IMUtil.getInstance().login(SystemValue.imid, SystemValue.imtoken, null);
        //todo 友盟推送 别名设置
//        PushAgent mPushAgent = PushAgent.getInstance(context);
//        mPushAgent.setAlias(mUserEntity.alias, "2048", new UTrack.ICallBack() {
//            @Override
//            public void onMessage(boolean isSuccess, String message) {
//                Log.i("友盟推送", "设置别名：isSuccess=" + isSuccess + ",message=" + message);
//            }
//        });
//        //todo 请求用户设置
//        new NewsRequestBusiness().userSet(new BaseSubscriber<ApiBaseResponse<UserSetBean>>() {
//            @Override
//            public void onError(ExceptionHandle.RespondThrowable e) {
//
//            }
//
//            @Override
//            public void onSuccess(ApiBaseResponse<UserSetBean> response, boolean isProcessed) {
//
//            }
//        });
        //EventBus.getDefault().post(new LoginNotifyEvent()); //todo 用户登录通知消息
        cacheUserData(json);
    }

    /**
     * 设置Cookie
     */
    private void syncCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<HttpCookie> cookies = new ArrayList<>();// 获取Cookie[可以是其他的方式获取]
        cookies.add(new HttpCookie("token", SystemValue.token));
        for (int i = 0; i < cookies.size(); i++) {
            HttpCookie cookie = cookies.get(i);
            String value = cookie.getName() + "=" + cookie.getValue();
            cookieManager.setCookie(UrlConst.BASE_URL, value);
        }
    }

    /**
     * 设置Cookie
     */
    private void clearCookie() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    public void initUserEntity() {
        String data = SPUtil.getInstance().getString( PreferenceConst.USER_DATA_CACHE, "");
        if (!TextUtils.isEmpty(data)) {
            this.mUserEntity = new Gson().fromJson(data, UserEntity.class);
            SystemValue.token = mUserEntity.token;
            SystemValue.imid = mUserEntity.imid;
            SystemValue.imtoken = mUserEntity.imtoken;
        }
    }

    public void getUserInfo(Activity activity) {
        ApiServiceWrapper.wrap(App.getServiceInterface().getUserInfo(), UserEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                        mUserEntity = response.getEntity();
                        mUserEntity.token = SystemValue.token;
                        cacheUserData(mUserEntity);
                        EventBus.getDefault().post(new UserInfoRefreshNotifyEvent());
                    }
                });
    }

    /**
     * 退出登录
     *
     * @param activity
     */
    public void clearData(Activity activity) {
        clearCookie();
        this.mUserEntity = null;
        SystemValue.token = "";
        SystemValue.imid = "";
        SystemValue.imtoken = "";
        syncCookie();
//        IMUtil.getInstance().logout(); //todo im注销
//        if(mUserEntity!=null)  //todo 友盟推送，别名删除
//        {
//            PushAgent.getInstance(BaseApplication.getContext()).deleteAlias(mUserEntity.alias, "2048", new UTrack.ICallBack() {
//                @Override
//                public void onMessage(boolean isSuccess, String message) {
//                    Log.i("友盟推送", "重置别名为空：isSuccess=" + isSuccess + ",message=" + message);
//                }
//            });
//        }
        EventBus.getDefault().post(new LogoutNotifyEvent()); //退出登录 通知消息
        CacheRepository.getInstance().clearTempEditPostBean();//清空 帖子编辑缓存
        CacheRepository.getInstance().clearPostCommentCacheBean(); //清空 (帖子/评论/回复)的评论缓存
        CacheRepository.getInstance().clearPostFilter(); //清空 帖子过滤id缓存

//        ThirdLoginUtil.deleteOauth(activity, SHARE_MEDIA.WEIXIN); //删除 第三方微信授权
//        ThirdLoginUtil.deleteOauth(activity, SHARE_MEDIA.QQ);   //删除 第三方QQ授权
        SPUtil.getInstance().putString(PreferenceConst.USER_DATA_CACHE, ""); //清空 用户数据缓存
        SPUtil.getInstance().putString(PreferenceConst.SP_CJ_USER_NAV_INFO_CACHE, ""); //清空 个人信息用户数据缓存
        // 还有 【赛事筛选】 和  【更多设置 红/黄牌】 跟用户无关联，不需要删除
    }

    /**
     * 刷新token
     *
     * @param token
     */
    public void refreshToken(String token) {
        SystemValue.token = token;
        if (mUserEntity != null) {
            mUserEntity.token = token;
            cacheUserData(mUserEntity);
        }
    }

    /**
     * 刷新imToken
     *
     * @param imToken
     */
    public void refreshIMToken(String imToken) {
        SystemValue.imtoken = imToken;
        if (mUserEntity != null) {
            mUserEntity.imtoken = imToken;
            cacheUserData(mUserEntity);
        }
    }

    /**
     * Token更新 ImToken 更新 用户信息更新 重新缓存UserEntity
     */
    public void cacheUserData(UserEntity entity) {
        cacheUserData(MGson.newGson().toJson(entity));
    }

    public void cacheUserData(String json) {
        SPUtil.getInstance().putString(PreferenceConst.USER_DATA_CACHE, json);
    }
}
