package com.github.obelieve.community.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.obelieve.App;
import com.github.obelieve.community.bean.CommentListEntity;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.bean.UpdateDetailEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.ui.CommonDialog;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiErrorCode;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.info.TelephoneUtil;


public class UpdatesViewModel extends ViewModel {

    private int mUpdatesPage = 1;
    private String keyword = "";

    //广场列表数据
    private MutableLiveData<SquareListsEntity> squareListsEntityMutableLiveData = new MutableLiveData<>();

    //帖子详情数据
    private MutableLiveData<UpdateDetailEntity> updateDetailEntityMutableLiveData = new MutableLiveData<>();

    //帖子评论列表数据
    private MutableLiveData<CommentListEntity> commentListEntityMutableLiveData = new MutableLiveData<>();

    //刷新数据
    private MutableLiveData<Boolean> mRefreshLiveData = new MutableLiveData<>();
    //加载更多
    private MutableLiveData<BaseRecyclerViewAdapter.LoadMoreStatus> mLoadMoreStatusMutableLiveData = new MutableLiveData<>();

    //是否加载
    private MutableLiveData<Boolean> showdialog = new MutableLiveData<>();

    //评论发布成功
    private MutableLiveData<Boolean> commnetSuccess = new MutableLiveData<>();

    //点赞成功 是否取消
    private MutableLiveData<Boolean> likeOrNo = new MutableLiveData<>();

    /**
     * 获取帖子数据
     *
     * @param isMore 是否加载更多
     */
    public void getSqareUpates(Activity activity, boolean isMore) {
        if (!isMore) {
            mUpdatesPage = 1;
        }
        ApiServiceWrapper.squarePost(activity, mUpdatesPage, keyword, new ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>>() {
            @Override
            public void onError(ApiServiceException e) {
                Log.e("RespondThrowable", ""+e.getMessage());
                if(!isMore){
                    mRefreshLiveData.setValue(false);
                }else{
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.ERROR);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<SquareListsEntity> response, boolean isProcessed) {
                SquareListsEntity data = response.getEntity();
                //是否为顶部数据
                if (data.getPost_list() != null && data.getPost_list().size() > 0) {
                    mUpdatesPage = data.getCurrent_page() + 1;
                }
                squareListsEntityMutableLiveData.setValue(data);
                if(!isMore){
                    mRefreshLiveData.setValue(false);
                }
                if(data.getHas_next_page()==1){
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.LOADING);
                }else{
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.END);
                }
            }
        });
    }

    /**
     * 获取精选数据
     *
     * @param isMore 是否加载更多
     */
    public void getChoiceness(Activity activity, boolean isMore) {
        if (!isMore) {
            mUpdatesPage = 1;
        }
        ApiServiceWrapper.goodPost(activity, mUpdatesPage, new ApiBaseSubscribe<ApiBaseResponse<SquareListsEntity>>() {
            @Override
            public void onError(ApiServiceException e) {
                Log.e("RespondThrowable", e.getMessage());
                if(!isMore){
                    mRefreshLiveData.setValue(false);
                }else{
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.ERROR);
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<SquareListsEntity> response, boolean isProcessed) {
                SquareListsEntity data = response.getEntity();
                //是否为顶部数据
                squareListsEntityMutableLiveData.setValue(data);
                if (data.getPost_list() != null && data.getPost_list().size() > 0) {
                    mUpdatesPage = data.getCurrent_page() + 1;
                }
                if(!isMore){
                    mRefreshLiveData.setValue(false);
                }
                if(data.getHas_next_page()==1){
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.LOADING);
                }else{
                    mLoadMoreStatusMutableLiveData.setValue(BaseRecyclerViewAdapter.LoadMoreStatus.END);
                }
            }
        });
    }

    /**
     * 获取详情数据
     *
     * @param postId
     */
    public void getUpateDetail(Activity activity, int postId) {
        mCommentPage = 0;
        ApiService.wrap(App.getServiceInterface().postDetail(postId), UpdateDetailEntity.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<UpdateDetailEntity>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                Log.e("RespondThrowable", e.getMessage());
                mRefreshLiveData.setValue(false);
            }

            @Override
            public void onSuccess(ApiBaseResponse<UpdateDetailEntity> response, boolean isProcessed) {
                UpdateDetailEntity data = response.getEntity();
                updateDetailEntityMutableLiveData.postValue(data);
                mRefreshLiveData.setValue(false);
            }
        });
    }

    public int getCommentPage() {
        return mCommentPage;
    }

    int mCommentPage = 0;
    String commentSortType = "created_at";

    /**
     * 获取评论
     */
    public void getCommentList(Activity activity, int postId, int fromCId) {
        ApiService.wrap(App.getServiceInterface().commentList(postId, mCommentPage, commentSortType, fromCId != 0 ? fromCId : null), CommentListEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<CommentListEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        Log.e("RespondThrowable", e.getMessage());
                        //page>1代表是获取更多评论，则关闭页面的上拉加载更多
                        if (mCommentPage > 1) {
                            mRefreshLiveData.setValue(false);
                        }
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<CommentListEntity> response, boolean isProcessed) {
                        CommentListEntity data = response.getEntity();
                        commentListEntityMutableLiveData.setValue(data);
                        //page>1代表是获取更多评论，则关闭页面的上拉加载更多
                        if (mCommentPage > 1) {
                            mRefreshLiveData.setValue(false);
                        }
                        mCommentPage = data.getCurrent_page() + 1;
                    }
                });
    }

    public void zan(Activity activity, int postId, int commentId, boolean like) {
        if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
            return;
        }
        likeOrNo.setValue(!like);
        ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(postId, commentId != 0 ? commentId : null), PraiseEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>(activity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        Log.e("RespondThrowable", ""+e.getMessage());
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                    }
                });
    }

    /**
     * 发布评论
     */
    public void sendComment(Activity activity, int postId, String content) {
        showdialog.postValue(true);
        ApiService.wrap(App.getServiceInterface().sendPostComment(postId, content), String.class).subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
            @Override
            public void onError(ApiServiceException e) {
                showdialog.postValue(false);
                if (e.getCode() == ApiErrorCode.CODE_NOSET_NICKNAME) {
                    if (activity != null) {
                        CommonDialog dialog = new CommonDialog(activity);
                        dialog.setContent("您还未设置昵称");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("去设置", dialog1 -> {
                            dialog1.dismiss();
                            ActivityUtil.gotoUserInfoActivity(activity, false, true);
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                showdialog.postValue(false);
                commnetSuccess.postValue(isProcessed);
            }
        });
    }

    public MutableLiveData<Boolean> getCommnetSuccess() {
        return commnetSuccess;
    }

    public MutableLiveData<CommentListEntity> getCommentListEntityMutableLiveData() {
        return commentListEntityMutableLiveData;
    }

    public MutableLiveData<SquareListsEntity> getSquareListsEntityMutableLiveData() {
        return squareListsEntityMutableLiveData;
    }

    public MutableLiveData<UpdateDetailEntity> getUpdateDetailEntityMutableLiveData() {
        return updateDetailEntityMutableLiveData;
    }

    public MutableLiveData<Boolean> getRefreshLiveData() {
        return mRefreshLiveData;
    }

    public MutableLiveData<BaseRecyclerViewAdapter.LoadMoreStatus> getLoadMoreStatusMutableLiveData() {
        return mLoadMoreStatusMutableLiveData;
    }

    public String getCommentSortType() {
        return commentSortType;
    }

    public void setCommentSortType(String commentSortType) {
        this.commentSortType = commentSortType;
    }

    public MutableLiveData<Boolean> getLikeOrNo() {
        return likeOrNo;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public MutableLiveData<Boolean> getShowdialog() {
        return showdialog;
    }
}
