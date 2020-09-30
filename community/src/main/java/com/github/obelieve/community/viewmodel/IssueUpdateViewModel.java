package com.github.obelieve.community.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.ui.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiErrorCode;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.ToastUtil;


public class IssueUpdateViewModel extends ViewModel {

    public static class State{
        //发送中
        public static int SENDING=1;
        //发送失败
        public static int SENDERROR=2;
        //发送成功
        public static int SENDSUCCEED=3;
    }

    private MutableLiveData<Integer> isSendstate=new MutableLiveData<>();
    /**
     * 发布帖子
     */
    public void sendUpdata(Activity activity,String content, String media, String media_scale){
        isSendstate.postValue(State.SENDING);
        ApiServiceWrapper.sendPost(content, media, media_scale, new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                isSendstate.postValue(State.SENDERROR);
                if(e.code== ApiErrorCode.CODE_NOSET_NICKNAME){
                    CommonDialog dialog = new CommonDialog(activity);
                    dialog.setContent("给自己起一个好称呼,让网友能更好认出你");
                    dialog.setNegativeButton("好的", null);
                    dialog.setPositiveButton("去设置", dialog1 -> {
                        dialog1.dismiss();
                        ActivityUtil.gotoUserInfoActivity(activity,false,true);
                    });
                    dialog.show();
                    e.isProcessed=true;
                }
                ToastUtil.show(e.message);
            }

            @Override
            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                isSendstate.postValue(State.SENDSUCCEED);
            }
        });
    }

    public MutableLiveData<Integer> getIsSendstate() {
        return isSendstate;
    }
}
