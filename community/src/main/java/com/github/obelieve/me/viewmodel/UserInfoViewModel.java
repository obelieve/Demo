package com.github.obelieve.me.viewmodel;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.cache.UserHelper;
import com.github.obelieve.repository.cache.constant.PreferenceConst;
import com.github.obelieve.thirdsdklib.QiNiuUploadUtil;
import com.github.obelieve.utils.others.SystemConstant;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.BitmapUtil;
import com.zxy.frame.utils.FileUtil;
import com.zxy.frame.utils.SPUtil;
import com.zxy.frame.utils.ToastUtil;

import java.io.File;
import java.util.List;

/**
 * Created by Admin
 * on 2020/9/11
 */
public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<Boolean> mShowProgressMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> mSuccessMutableLiveData = new MutableLiveData<>();

    public void updateUserInfo(Activity activity, final String nickname, final String sex, final String city, final String province, final String birthday, String imagePath) {
        mShowProgressMutableLiveData.postValue(true);
        if (!TextUtils.isEmpty(imagePath)) {
            File file = new File(imagePath);
            if (file.length() > FileUtil.MB) {
                String compressImage = BitmapUtil.compressImage(imagePath, SystemConstant.TEMP_IMAGE_PATH + "temp" + 0 + ".png", 30);
                file = new File(compressImage);
            }
            QiNiuUploadUtil.getInstance().upload(file, new QiNiuUploadUtil.Callback() {
                @Override
                public void getToken(String token) {
                    SPUtil.getInstance().putString(PreferenceConst.UPLOAD_TOKEN, token);
                }

                @Override
                public void onSuccess(List<String> urlList) {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    Log.i("qiniu", "Upload Success " + urlList);
                    upDateUserInfo(activity, nickname, sex, city, province, birthday, (urlList != null && urlList.size() > 0) ? urlList.get(0) : "");
                }

                @Override
                public void onFailure(String msg) {
                    mShowProgressMutableLiveData.postValue(false);
                    Log.e("qiniu", "上传失败!=" + msg + " " + Thread.currentThread().toString());
                    ToastUtil.show("图片上传失败");

                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
            }, activity);
        } else {
            upDateUserInfo(activity, nickname, sex, city, province, birthday, "");
        }
    }

    /**
     * 服务器提交用户信息
     *
     * @param activity
     * @param nickname
     * @param sex
     * @param city
     * @param province
     * @param birthday
     * @param urlPath
     */
    private void upDateUserInfo(Activity activity, String nickname, String sex, String city, String province, String birthday, String urlPath) {
        ApiServiceWrapper.updateUserInfo(activity, nickname, sex, city, province, birthday, urlPath, new ApiBaseSubscribe<ApiBaseResponse<String>>() {
            @Override
            public void onError(ApiServiceException e) {
                mShowProgressMutableLiveData.postValue(false);
                ToastUtil.show(e.message);
            }

            @Override
            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                ToastUtil.show(response.getMsg());
                UserHelper.getInstance().getUserInfo(activity);
                mShowProgressMutableLiveData.postValue(false);
                mSuccessMutableLiveData.postValue(response.getEntity());
            }
        });
    }

    public MutableLiveData<Boolean> getShowProgressMutableLiveData() {
        return mShowProgressMutableLiveData;
    }

    public MutableLiveData<String> getSuccessMutableLiveData() {
        return mSuccessMutableLiveData;
    }
}
