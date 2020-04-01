package com.zxy.demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zxy.demo.entity.VersionUpdateEntity;
import com.zxy.demo.http.ApiService;
import com.zxy.frame.net.BaseResponse;
import com.zxy.frame.net.BaseSubscribe;

public class VersionUpdateViewModel extends ViewModel {

    private MutableLiveData<Boolean> mLoadingLiveData = new MutableLiveData<>();
    private MutableLiveData<VersionUpdateEntity> mVersionUpdateEntityLiveData = new MutableLiveData<>();

    public void version_check() {
        mLoadingLiveData.postValue(true);
        ApiService.getInstance().version_check().subscribe(new BaseSubscribe<BaseResponse<VersionUpdateEntity>>() {
            @Override
            public void onNext(BaseResponse<VersionUpdateEntity> response) {
                mLoadingLiveData.postValue(false);
                mVersionUpdateEntityLiveData.postValue(response.getData(VersionUpdateEntity.class));
            }

            @Override
            public void onError(Throwable e) {
                mLoadingLiveData.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return mLoadingLiveData;
    }

    public void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData) {
        mLoadingLiveData = loadingLiveData;
    }

    public MutableLiveData<VersionUpdateEntity> getVersionUpdateEntityLiveData() {
        return mVersionUpdateEntityLiveData;
    }

    public void setVersionUpdateEntityLiveData(MutableLiveData<VersionUpdateEntity> versionUpdateEntityLiveData) {
        mVersionUpdateEntityLiveData = versionUpdateEntityLiveData;
    }
}
