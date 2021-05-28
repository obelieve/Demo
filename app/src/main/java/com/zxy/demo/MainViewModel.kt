package com.zxy.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.obelieve.frame.net.ApiBaseResponse
import com.obelieve.frame.net.ApiBaseSubscribe
import com.obelieve.frame.net.ApiService
import com.obelieve.frame.net.ApiServiceException
import com.obelieve.frame.view.PageStatusView

class MainViewModel : ViewModel() {


    var mPageStatusLiveData = MutableLiveData<PageStatusView.Status>()
    get() {return field}

    var mDiamondGoodInfoEntity = MutableLiveData<DiamondGoodEntity>()
        get() {return field}

    // 支付成功与否
    var mPaySuccessLiveData = MutableLiveData<Boolean>()
        get() {return field}

    var mShowLoadingLiveData = MutableLiveData<Boolean>()
        get() {return field}

    /**
     * 获取钻石页
     */
    fun fetchDiamondGoodInfo(isRefresh : Boolean){

        if(!isRefresh) {
            mPageStatusLiveData.value = PageStatusView.Status.LOADING
        }

        ApiService.wrap(
            App.getServiceInterface().getDiamondGoodList(),
            DiamondGoodEntity::class.java
        ).subscribe(object : ApiBaseSubscribe<ApiBaseResponse<DiamondGoodEntity>>(){
            override fun onError(e: ApiServiceException?) {
                mPageStatusLiveData.value = PageStatusView.Status.FAILURE
            }

            override fun onSuccess(t: ApiBaseResponse<DiamondGoodEntity>?, isProcessed: Boolean) {
                mPageStatusLiveData.value = PageStatusView.Status.SUCCESS
                t?.let{
                    mDiamondGoodInfoEntity.value = it.entity
                }
            }
        })

    }


    /**
     * 将付款信息发送至服务端
     */
    fun sendPayToken(reportData:String){
        mShowLoadingLiveData.value = true
        ApiService.wrap(
            App.getServiceInterface().sendPaymentReport(reportData),
            String::class.java
        ).subscribe(object : ApiBaseSubscribe<ApiBaseResponse<String>>(){
            override fun onError(e: ApiServiceException?) {
                mShowLoadingLiveData.value = false
                mPaySuccessLiveData.value = false
            }

            override fun onSuccess(t: ApiBaseResponse<String>?, isProcessed: Boolean) {
                mShowLoadingLiveData.value = false
                mPaySuccessLiveData.value = true
            }
        })
    }


}