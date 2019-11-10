package com.zxy.demo.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zxy.demo.App;
import com.zxy.demo.model.SquareListModel;
import com.zxy.frame.json.MGson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<SquareListModel.DataBean.PostListBean>> mListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRefreshLiveData = new MutableLiveData<>();
    private MutableLiveData<LoadMoreModel> mLoadMoreLiveData = new MutableLiveData<>();

    private int mCurPage;

    public void square_post(boolean isMore) {
        if (!isMore) {
            mCurPage = 1;
        }else{
            mCurPage++;
        }
        App.getServiceInterface().square_post(mCurPage).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    SquareListModel model = MGson.newGson().fromJson(json, SquareListModel.class);
                    List<SquareListModel.DataBean.PostListBean> beans = model.getData().getPost_list();
                    if (!isMore) {
                        mListMutableLiveData.setValue(model.getData().getPost_list());
                    } else {
                        List<SquareListModel.DataBean.PostListBean> oldBeans = mListMutableLiveData.getValue();
                        if (oldBeans != null) {
                            oldBeans.addAll(beans);
                            beans = oldBeans;
                        }
                        mListMutableLiveData.setValue(beans);
                    }
                    if (!isMore) {
                        if (model.getData().getHas_next_page() == 0) {
                            mLoadMoreLiveData.setValue(new LoadMoreModel(false, false, true));
                        }else{
                            mLoadMoreLiveData.setValue(new LoadMoreModel(true, false, false));
                        }
                    }else{
                        if (model.getData().getHas_next_page() == 0) {
                            mLoadMoreLiveData.setValue(new LoadMoreModel(false, false, true));
                        }else{
                            mLoadMoreLiveData.setValue(new LoadMoreModel(true, false, false));
                        }
                    }
                    mRefreshLiveData.setValue(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (!isMore) {
                        mRefreshLiveData.setValue(false);
                    }else{
                        mLoadMoreLiveData.setValue(new LoadMoreModel(false, true, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isMore) {
                    mLoadMoreLiveData.setValue(new LoadMoreModel(false, true, false));
                }else{
                    mRefreshLiveData.setValue(false);
                }
            }
        });
    }

    public MutableLiveData<List<SquareListModel.DataBean.PostListBean>> getListMutableLiveData() {
        return mListMutableLiveData;
    }

    public MutableLiveData<Boolean> getRefreshLiveData() {
        return mRefreshLiveData;
    }

    public MutableLiveData<LoadMoreModel> getLoadMoreLiveData() {
        return mLoadMoreLiveData;
    }

    public static class LoadMoreModel {

        private boolean mCompleted;
        private boolean mError;
        private boolean mEnd;

        public LoadMoreModel(boolean completed, boolean error, boolean end) {
            mCompleted = completed;
            mError = error;
            mEnd = end;
        }

        public boolean isCompleted() {
            return mCompleted;
        }

        public boolean isError() {
            return mError;
        }

        public boolean isEnd() {
            return mEnd;
        }
    }
}
