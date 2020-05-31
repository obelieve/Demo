package com.zxy.mall.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zxy.mall.entity.GoodEntity;
import com.zxy.mall.entity.SellerEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.http.ApiService;

public class StoreViewModel extends ViewModel {

    private MutableLiveData<Boolean> mShowProgressLiveData = new MutableLiveData<>();
    private MutableLiveData<SellerEntity> mSellerEntityLiveData = new MutableLiveData<>();

    public void storeInfo() {
        mShowProgressLiveData.postValue(true);
        MockRepository.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mShowProgressLiveData.postValue(false);
                mSellerEntityLiveData.setValue(MockRepository.getSeller());
            }
        },500);
    }

    public MutableLiveData<Boolean> getShowProgressLiveData() {
        return mShowProgressLiveData;
    }

    public MutableLiveData<SellerEntity> getSellerEntityLiveData() {
        return mSellerEntityLiveData;
    }
}
