package com.github.obelieve.login;

import android.app.Activity;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.App;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.login.entity.SendSMSEntity;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiServiceException;

public class LoginViewModel extends ViewModel {

    //UserEntity数据
    private MutableLiveData<UserEntity> mUserEntityMutableLiveData = new MutableLiveData<>();
    //SmsCode数据
    private MutableLiveData<SendSMSEntity> mSendSMSEntityMutableLiveData = new MutableLiveData<>();
    //已经绑定手机
    private MutableLiveData<Boolean> mBindPhoneMutableLiveData = new MutableLiveData<>();
    //是否加载
    private MutableLiveData<Boolean> mShowDialogMutableLiveData = new MutableLiveData<>();
    //是否加载失败
    private MutableLiveData<String> mLoadErrorMutableLiveData = new MutableLiveData<>();
    //绑定手机成功
    private MutableLiveData<ApiBaseResponse<String>> mBindMobileSuccessMutableLiveData = new MutableLiveData<>();
    //解绑手机成功
    private MutableLiveData<ApiBaseResponse<String>> mUnBindMobileSuccessMutableLiveData = new MutableLiveData<>();

    /**
     * 第三方登录绑定手机号时，获取验证码
     *
     * @param phone
     */
    public void getThirdBindSmsCode(Activity activity, String phone, String open_type, String open_id) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().sendSMS(phone, "2", open_type, open_id, null), SendSMSEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<SendSMSEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                        if (e.code == 30001) {
                            mBindPhoneMutableLiveData.postValue(true);
                        }
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<SendSMSEntity> response, boolean isProcessed) {
                        mShowDialogMutableLiveData.postValue(false);
                        mSendSMSEntityMutableLiveData.postValue(response.getEntity());
                        mBindPhoneMutableLiveData.postValue(false);
                    }
                });
    }

    public void getSmsCode(Activity activity, String phone, int smsType) {
        getSmsCode(activity, phone, smsType, null);
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param smsType 短信类型；1-登录注册 2-绑定手机号（第三方登录时） 3-解绑手机号码 4-绑定手机号码（设置）
     */
    public void getSmsCode(Activity activity, String phone, int smsType, String validate_code) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().sendSMS(phone, smsType + "", "", "", validate_code), SendSMSEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<SendSMSEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<SendSMSEntity> response, boolean isProcessed) {
                        mShowDialogMutableLiveData.postValue(false);
                        if (!TextUtils.isEmpty(response.getData()) && response.getData().contains("validate_code")) {
                            mLoadErrorMutableLiveData.postValue(response.getMsg());
                        }
                        mSendSMSEntityMutableLiveData.postValue(response.getEntity());
                    }
                });
    }

    /**
     * 登录
     *
     * @param phone
     * @param code
     */
    public void login(Activity activity, String phone, String code) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().login(phone, code, null), UserEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                        CacheRepository.getInstance().setUserEntity(response.getData(), response.getEntity(), activity);
                        mUserEntityMutableLiveData.postValue(response.getEntity());
                        mShowDialogMutableLiveData.postValue(false);
                    }
                });
    }

    /**
     * 第三方登录
     *
     * @param open_type
     * @param open_id
     * @param access_token
     */
    public void thridLogin(Activity activity, String open_type, String open_id, String access_token) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.thridLogin(open_type, open_id, access_token, new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                mShowDialogMutableLiveData.postValue(false);
                mLoadErrorMutableLiveData.postValue(e.message);
            }

            @Override
            public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                UserEntity entity = response.getEntity();
                if (entity.do_type.equals("login")) {
                    CacheRepository.getInstance().setUserEntity(response.getData(), entity, activity);
                }
                mUserEntityMutableLiveData.postValue(entity);
                mShowDialogMutableLiveData.postValue(false);
            }
        });
    }

    /**
     * 第三方绑定手机
     *
     * @param phone
     * @param code
     * @param openType
     * @param openID
     */
    public void thridBindPhone(Activity activity, String phone, String code, String openType, String openID) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.thridBindPhone(phone, code, openType, openID, new ApiBaseSubscribe<ApiBaseResponse<UserEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                mShowDialogMutableLiveData.postValue(false);
                mLoadErrorMutableLiveData.postValue(e.message);
            }

            @Override
            public void onSuccess(ApiBaseResponse<UserEntity> response, boolean isProcessed) {
                UserEntity entity = response.getEntity();
                CacheRepository.getInstance().setUserEntity(response.getData(), entity, activity);
                mUserEntityMutableLiveData.postValue(entity);
                mShowDialogMutableLiveData.postValue(false);
            }
        });
    }


    /**
     * 解绑手机
     *
     * @param code
     */
    public void unbindMobile(Activity activity, String code) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().unbindMobile(code), String.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                        mUnBindMobileSuccessMutableLiveData.postValue(response);
                        mShowDialogMutableLiveData.postValue(false);
                    }
                });
    }

    /**
     * 绑定手机
     *
     * @param phone
     * @param code
     */
    public void bindMobile(Activity activity, String phone, String code) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().bindMobile(phone, code), String.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                        mBindMobileSuccessMutableLiveData.postValue(response);
                        mShowDialogMutableLiveData.postValue(false);
                    }
                });
    }


    /**
     * 账号注销
     *
     * @param phone
     * @param code
     */
    public void cancelAccount(Activity activity, String phone, String code) {
        mShowDialogMutableLiveData.postValue(true);
        ApiServiceWrapper.wrap(App.getServiceInterface().cancelAccount(phone, code), String.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        mShowDialogMutableLiveData.postValue(false);
                        mLoadErrorMutableLiveData.postValue(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                        mUserEntityMutableLiveData.postValue(null);
                        mShowDialogMutableLiveData.postValue(false);
                    }
                });
    }

    public MutableLiveData<UserEntity> getUserEntityMutableLiveData() {
        return mUserEntityMutableLiveData;
    }

    public MutableLiveData<Boolean> getShowDialogMutableLiveData() {
        return mShowDialogMutableLiveData;
    }

    public MutableLiveData<String> getLoadErrorMutableLiveData() {
        return mLoadErrorMutableLiveData;
    }

    public MutableLiveData<Boolean> getBindPhoneMutableLiveData() {
        return mBindPhoneMutableLiveData;
    }

    public MutableLiveData<SendSMSEntity> getSendSMSEntityMutableLiveData() {
        return mSendSMSEntityMutableLiveData;
    }

    public MutableLiveData<ApiBaseResponse<String>> getBindMobileSuccessMutableLiveData() {
        return mBindMobileSuccessMutableLiveData;
    }

    public MutableLiveData<ApiBaseResponse<String>> getUnBindMobileSuccessMutableLiveData() {
        return mUnBindMobileSuccessMutableLiveData;
    }
}
