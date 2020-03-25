package com.zxy.demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zxy.demo.entity.SquarePostEntity;
import com.zxy.demo.http.ApiService;
import com.zxy.frame.net.BaseResponse;
import com.zxy.frame.net.BaseSubscribe;

import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<SquarePostEntity.PostListBean>> mListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mRefreshLiveData = new MutableLiveData<>();
    private MutableLiveData<LoadMoreModel> mLoadMoreLiveData = new MutableLiveData<>();

    private int mCurPage;

    public void square_post(boolean isMore) {
        if (!isMore) {
            mCurPage = 1;
        } else {
            mCurPage++;
        }
        ApiService.getInstance().square_post(1).subscribe(new BaseSubscribe<BaseResponse<SquarePostEntity>>() {
            @Override
            public void onNext(BaseResponse<SquarePostEntity> response) {
                success(response.getData(SquarePostEntity.class),isMore);
            }

            @Override
            public void onError(Throwable e) {
                failure(isMore);
            }
        });
    }


    private void success(SquarePostEntity entity, boolean isMore) {
        List<SquarePostEntity.PostListBean> beans = entity.getPost_list();
        if (!isMore) {
            mListMutableLiveData.setValue(entity.getPost_list());
        } else {
            List<SquarePostEntity.PostListBean> oldBeans = mListMutableLiveData.getValue();
            if (oldBeans != null) {
                oldBeans.addAll(beans);
                beans = oldBeans;
            }
            mListMutableLiveData.setValue(beans);
        }
        if (!isMore) {
            if (entity.getHas_next_page() == 0) {
                mLoadMoreLiveData.setValue(new LoadMoreModel(false, false, true));
            } else {
                mLoadMoreLiveData.setValue(new LoadMoreModel(true, false, false));
            }
        } else {
            if (entity.getHas_next_page() == 0) {
                mLoadMoreLiveData.setValue(new LoadMoreModel(false, false, true));
            } else {
                mLoadMoreLiveData.setValue(new LoadMoreModel(true, false, false));
            }
        }
        mRefreshLiveData.setValue(false);
    }

    private void failure(boolean isMore) {
        if (isMore) {
            mLoadMoreLiveData.setValue(new LoadMoreModel(false, true, false));
        } else {
            mRefreshLiveData.setValue(false);
        }
    }

    public MutableLiveData<List<SquarePostEntity.PostListBean>> getListMutableLiveData() {
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
