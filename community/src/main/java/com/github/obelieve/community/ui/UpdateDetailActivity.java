package com.github.obelieve.community.ui;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.CommentListAdapter;
import com.github.obelieve.community.adapter.viewholder.UpdateDetailViewHolder;
import com.github.obelieve.community.bean.CommentListEntity;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.bean.UpdateDetailEntity;
import com.github.obelieve.community.viewmodel.SendCommentModule;
import com.github.obelieve.community.viewmodel.UpdatesViewModel;
import com.github.obelieve.event.community.CommunityItemChangedEvent;
import com.github.obelieve.event.community.PostCommentDelRefreshEvent;
import com.github.obelieve.event.community.PostFilterCacheEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.github.obelieve.utils.BottomMenuUtil;
import com.github.obelieve.utils.TelephoneUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.LogUtil;
import com.zxy.frame.utils.SystemUtil;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.image.GlideUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateDetailActivity extends ApiBaseActivity {

    public static final String FROM_CID = "from_cid";
    public static final String POST_ID="post_id";
    public static final String USER_ID="user_id";
    public static final String TAG="tag";

    UpdatesViewModel mUpdatesViewModel;
    //帖子Id
    int mPostId;
    int mUserId;
    int mFromCId;
    String mTag;

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.srl_update_detail)
    SmartRefreshLayout srl_update_detail;
    @BindView(R.id.txt_likenum)
    TextView txtLikenum;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerUpdateDetail;
    @BindView(R.id.layout_content_detail)
    LinearLayout layoutContentDetail;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.edit_comment_content)
    EditText editCommentContent;
    @BindView(R.id.cb_like)
    CheckBox cbLike;
    @BindView(R.id.layout_like)
    LinearLayout layoutLike;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.view_lottie_zan)
    LottieAnimationView viewLottieZan;

    CommentListAdapter mCommentListAdapter;

    String detailUserName;
    int detailUserBottom;
    boolean mIsZan;//是否点赞
    private int mZanNum;//点赞数量
    private int mPcNum;//评论数量
    SendCommentModule mSendCommentModule;

    View mLayoutCommentToolbar;
    TextView mTvCommentNum;
    CommonDialog mAlertDialog;
    UpdateDetailViewHolder mHeaderViewHolder;


    @Override
    protected int layoutId() {
        return R.layout.activity_update_detail;
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mPostId = getIntent().getIntExtra(POST_ID, 0);
        mUserId = getIntent().getIntExtra(USER_ID, 0);
        mFromCId = getIntent().getIntExtra(FROM_CID, 0);
        mTag = getIntent().getStringExtra(TAG);
        if (mPostId == 0) {
            ToastUtil.show("帖子详情不存在");
            finish();
        }
        mSendCommentModule = new SendCommentModule(this, mPostId);
        mUpdatesViewModel = ViewModelProviders.of(this).get(UpdatesViewModel.class);
        mUpdatesViewModel.getUpdateDetailEntityMutableLiveData().observe(this, updateDetailEntity -> {
            loaderView(updateDetailEntity);
            mIsZan = updateDetailEntity.getIs_zan() == 1;
            cbLike.setChecked(mIsZan);
            txtLikenum.setSelected(mIsZan);
        });
        mSendCommentModule.getVM().getShowdialog().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
        mSendCommentModule.getVM().getCommentSuccess().observe(this, aBoolean -> {
            if (mSendCommentModule.getDialog() != null) {
                mSendCommentModule.getDialog().dismiss();
            }
            if (!aBoolean) {
                ToastUtil.show("发布成功");
            }
            srl_update_detail.autoRefresh();
        });
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostCommentNumRefreshEvent(PostCommentDelRefreshEvent event) {
        if (mTvCommentNum != null) {
            mPcNum = mPcNum - 1;
            mTvCommentNum.setText("评论" + mPcNum);
            AppDataUtil.showZanComment(mTvCommentNum, "评论%s", mPcNum);
            notifyUpdatesFragmentData();//删除评论
        }
    }


    @OnClick({R.id.civ_avatar,R.id.rl_left, R.id.rl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar:
                ActivityUtil.gotoPersonalPageActivity(mActivity, mUserId);
                break;
            case R.id.rl_left:
                finish();
                break;
            case R.id.rl_right:
                showDeleteOrReport(mUserId, mPostId, "post");
                break;
        }
    }


    private void initView() {
        srl_update_detail.setEnableLoadMore(false);
        mCommentListAdapter = new CommentListAdapter(this, mSendCommentModule, mFromCId != 0);
        ((DefaultItemAnimator) recyclerUpdateDetail.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerUpdateDetail.setAdapter(mCommentListAdapter);
        recyclerUpdateDetail.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerUpdateDetail.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (parent.getAdapter() != null && position == parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = SystemUtil.dp2px(view.getContext(),25);
                }
            }
        });
        srl_update_detail.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mUpdatesViewModel.getUpateDetail(mActivity, mPostId);
                //todo 排序方式
                mUpdatesViewModel.getCommentList(mActivity, mPostId, mFromCId);
            }
        });
        srl_update_detail.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mUpdatesViewModel.getCommentList(mActivity, mPostId, mFromCId);
            }
        });
        //完成详情获取
        mUpdatesViewModel.getGetDataFinish().observe(this, updateDetailEntity -> {
            if (updateDetailEntity.booleanValue()) {
                srl_update_detail.finishLoadMore();
            } else {
                srl_update_detail.finishRefresh();
            }
        });
        //监听评论数据
        mUpdatesViewModel.getCommentListEntityMutableLiveData().observe(this, commentLitsEntity -> {
            List<CommentListEntity.DataBean> commentList = commentLitsEntity.getData();
            if (commentLitsEntity.getCurrent_page() <= 1) {
                mCommentListAdapter.getDataHolder().setList(commentList);
            } else {
                mCommentListAdapter.getDataHolder().addAll(commentList);
            }
            if (mCommentListAdapter.getItemCount() == 0) {
                tv_no_data.setVisibility(View.VISIBLE);
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
            //是否有一下页数据
            srl_update_detail.setEnableLoadMore(commentLitsEntity.getCurrent_page() != commentLitsEntity.getLast_page());
            mCommentListAdapter.notifyDataSetChanged();
        });
        //滑动到姓名不可见时标题栏现实姓名
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (detailUserBottom == 0) {
                View viewById = scrollView.findViewById(R.id.txt_user_name);
                if (viewById != null) {
                    detailUserBottom = viewById.getBottom() + SystemUtil.dp2px(App.getContext(), 20);
                    LogUtil.v("detailUserBottom", detailUserBottom + "");
                } else {
                    return;
                }
            } else {
                if (scrollY > detailUserBottom) {
                    setMyTitle(detailUserName);
                } else {
                    setMyTitle("");
                }
            }
        });
        mUpdatesViewModel.getShowdialog().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
        mUpdatesViewModel.getCommnetSuccess().observe(this, aBoolean -> {
            mPcNum = mPcNum + 1;
            editCommentContent.setText("");
            if (!aBoolean) {
                ToastUtil.show("发布成功");
            }
            srl_update_detail.autoRefresh();
            CacheRepository.getInstance().setPostComment(mPostId, "");
            notifyUpdatesFragmentData();//发布评论
        });

        mUpdatesViewModel.getLikeOrNo().observe(this, aBoolean -> {
            mIsZan = aBoolean;
            mZanNum = aBoolean ? mZanNum + 1 : mZanNum - 1;
            cbLike.setChecked(aBoolean);
            txtLikenum.setSelected(aBoolean);
            txtLikenum.setText(mZanNum + "");
            AppDataUtil.showZanComment(txtLikenum, mZanNum);
            notifyUpdatesFragmentData();//点赞
        });
        editCommentContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(mActivity);
                        editCommentContent.setFocusable(false);
                        editCommentContent.setFocusableInTouchMode(false);
                        return;
                    } else {
                        assertEmptyNickname();
                    }
                }
            }
        });
        editCommentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.toString().length();
                layoutLike.setVisibility(length > 0 ? View.GONE : View.VISIBLE);
                btnSend.setVisibility(length > 0 ? View.VISIBLE : View.GONE);
                if (length == 0) {
                    CacheRepository.getInstance().setPostComment(mPostId, "");
                }
            }
        });
        editCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppDataUtil.isAlertShow(mActivity)) {
                    return;
                } else {
                    editCommentContent.setFocusable(true);
                    editCommentContent.setFocusableInTouchMode(true);
                    editCommentContent.requestFocus();
                    InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editCommentContent, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        String tempComment = CacheRepository.getInstance().getPostComment(mPostId);
        if (!TextUtils.isEmpty(tempComment)) {
            editCommentContent.setText(tempComment);
        }
        //完成详情获取
        srl_update_detail.autoRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHeaderViewHolder != null) {
            mHeaderViewHolder.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHeaderViewHolder != null) {
            mHeaderViewHolder.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        saveCommentCache();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void saveCommentCache() {
        if (editCommentContent != null && !TextUtils.isEmpty(editCommentContent.getText().toString()) && editCommentContent.getText().toString().length() > 0) {
            final String content = editCommentContent.getText().toString();
            CacheRepository.getInstance().setPostComment(mPostId, content);
        }
    }

    private void loaderView(UpdateDetailEntity updateDetailEntity) {
        mUserId = updateDetailEntity.getUser_id();
        mPcNum = updateDetailEntity.getPc_num();
        mZanNum = updateDetailEntity.getZan_num();
        detailUserName = updateDetailEntity.getNickname();
        GlideUtil.loadImage(mActivity,updateDetailEntity.getAvatar(),civAvatar,R.drawable.me_face,R.drawable.me_face);
        tvName.setText(updateDetailEntity.getNickname());
        //点赞数
        txtLikenum.setText(mZanNum + "");
        AppDataUtil.showZanComment(txtLikenum, mZanNum);
        layoutContentDetail.removeAllViews();
        //添加详情视图
        mHeaderViewHolder = new UpdateDetailViewHolder(layoutContentDetail);
        mHeaderViewHolder.bind(updateDetailEntity);
        layoutContentDetail.addView(mHeaderViewHolder.itemView);
        //添加评论头部
        mLayoutCommentToolbar = LayoutInflater.from(mActivity).inflate(R.layout.include_comment_toolbar, null, false);
        mTvCommentNum = mLayoutCommentToolbar.findViewById(R.id.txt_comment_num);
        TextView tv_sort_time = mLayoutCommentToolbar.findViewById(R.id.tv_sort_time);
        TextView tv_sort_hot = mLayoutCommentToolbar.findViewById(R.id.tv_sort_hot);
        mTvCommentNum.setText("评论" + mPcNum);
        AppDataUtil.showZanComment(mTvCommentNum, "评论%s", mPcNum);
        if ("created_at".equals(mUpdatesViewModel.getCommentSortType())) {
            tv_sort_time.setSelected(true);
            tv_sort_hot.setSelected(false);
        } else {
            tv_sort_time.setSelected(false);
            tv_sort_hot.setSelected(true);
        }
        tv_sort_time.setOnClickListener(view -> {
            if (!tv_sort_time.isSelected()) {
                tv_sort_time.setSelected(true);
                tv_sort_hot.setSelected(false);
                mUpdatesViewModel.setCommentSortType("created_at");
                srl_update_detail.autoRefresh();
            }
        });
        tv_sort_hot.setOnClickListener(v -> {
            if (!tv_sort_hot.isSelected()) {
                tv_sort_time.setSelected(false);
                tv_sort_hot.setSelected(true);
                mUpdatesViewModel.setCommentSortType("zan_num");
                srl_update_detail.autoRefresh();
            }
        });
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, SystemUtil.dp2px(App.getContext(), 10), 0, 0);
        mLayoutCommentToolbar.setLayoutParams(layoutParams);
        layoutContentDetail.addView(mLayoutCommentToolbar);
    }


    private void assertEmptyNickname() {
        UserEntity entity = CacheRepository.getInstance().getUserEntity();
        if (entity != null && TextUtils.isEmpty(entity.nickname)) {
            if (mActivity != null) {
                editCommentContent.setFocusable(false);
                editCommentContent.setFocusableInTouchMode(false);
                CommonDialog dialog = new CommonDialog(mActivity);
                dialog.setContent("您还未设置昵称");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("去设置", dialog1 -> {
                    dialog1.dismiss();
                    ActivityUtil.gotoUserInfoActivity(mActivity, false, true);
                });
                dialog.show();
            }
        }
    }

    /**
     * 更新社区帖子数据
     */
    private void notifyUpdatesFragmentData() {
        if (mTag != null) {
            if (mTag.equals(UpdatesFragment.class.getSimpleName())) {
                EventBus.getDefault().post(new CommunityItemChangedEvent(mPostId, mPcNum, mZanNum, mIsZan));
            }
            //个人主页
            else if (mTag.equals(PersonalPageTabFragment.class.getSimpleName())) {
                EventBus.getDefault().post(new CommunityItemChangedEvent(mPostId, mPcNum, mZanNum, mIsZan));
            }
        }
    }

    /**
     * 显示举报或删除
     *
     * @param userId
     * @param p_id
     * @param p_type
     */
    private void showDeleteOrReport(int userId, int p_id, String p_type) {
        UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
        int curUserId = userEntity != null ? userEntity.user_id : 0;
        boolean isCurUser = curUserId == userId;
        String[] items = null;
        if (isCurUser) {
            items = new String[]{"删除", "取消"};
        } else {
            items = new String[]{"举报", "不感兴趣", "屏蔽此人", "取消"};
        }
        BottomMenuUtil.show(mActivity, items, (text, index) -> {
            //返回参数 text 即菜单名称，index 即菜单索引
            switch (index) {
                case 0:
                    if (isCurUser) {
                        showDelDialog(p_id, p_type);
                    } else {
                        if (TextUtils.isEmpty(SystemValue.token)) {
                            ActivityUtil.gotoLoginActivity(this);
                            return;
                        }
                        showReport(p_id, p_type);
                    }
                    break;
                case 1:
                    if (isCurUser) {
                        return;
                    } else {
                        if (TextUtils.isEmpty(SystemValue.token)) {
                            ActivityUtil.gotoLoginActivity(this);
                            return;
                        }
                        PostFilterCacheEvent cache = new PostFilterCacheEvent(mTag, mPostId, 0);
                        CacheRepository.getInstance().addPostIdFromPostFilter(mPostId);
                        EventBus.getDefault().post(cache);
                        ToastUtil.show("我们将减少这方面内容的推送");
                        finish();
                    }
                    break;
                case 2:
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(this);
                        return;
                    }
                    PostFilterCacheEvent cache2 = new PostFilterCacheEvent(mTag, 0, mUserId);
                    CacheRepository.getInstance().addUserIdPostFilter(mUserId);
                    EventBus.getDefault().post(cache2);
                    ToastUtil.show("将为你过滤此用户所发的内容");
                    finish();
                    break;
            }
        });
    }

    private void showDelDialog(int p_id, String p_type) {
        if (mAlertDialog == null) {
            mAlertDialog = new CommonDialog(mActivity);
            mAlertDialog.setContent("是否确认要删除？");
            mAlertDialog.setNegativeButton("取消", new CommonDialog.onCancelListener() {
                @Override
                public void onClickCancel(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            mAlertDialog.setPositiveButton("确定", new CommonDialog.onSubmitListener() {
                @Override
                public void onClickSubmit(Dialog dialog) {
                    dialog.dismiss();
                    ApiService.wrap(App.getServiceInterface().delPost(p_id, p_type), String.class)
                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mActivity) {
                                @Override
                                public void onError(ApiServiceException e) {
                                    ToastUtil.show("删除失败！");
                                }

                                @Override
                                public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                    ToastUtil.show(response.getMsg());
                                    finish();
                                }
                            });
                }
            });
        } else {
            mAlertDialog.dismiss();
        }
        mAlertDialog.show();
    }

    /**
     * 举报
     *
     * @param p_id   帖子ID\评论ID\回复ID
     * @param p_type 举报类型 post-帖子；comment-评论；reply-回复
     */
    private void showReport(int p_id, String p_type) {
        showLoading();
        ApiService.wrap(App.getServiceInterface().getReportType(), ReportTypeEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<ReportTypeEntity>>(mActivity) {
                    @Override
                    public void onError(ApiServiceException e) {
                        dismissLoading();
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<ReportTypeEntity> response, boolean isProcessed) {
                        dismissLoading();
                        try {
                            List<ReportTypeEntity.ReportTypeBean> list = response.getEntity() != null ? response.getEntity().getReport_type() : new ArrayList<>();
                            int[] ids = new int[list.size()];
                            String[] names = new String[list.size() + 1];
                            for (int i = 0; i < list.size(); i++) {
                                ids[i] = list.get(i).getRt_id();
                                names[i] = list.get(i).getRt_name();
                            }
                            names[list.size()] = "取消";
                            BottomMenuUtil.show(mActivity, names, (text2, index2) -> {
                                if (index2 != list.size()) {
                                    ApiService.wrap(App.getServiceInterface().reportPost(p_id, ids[index2], p_type), String.class)
                                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mActivity) {
                                                @Override
                                                public void onError(ApiServiceException e) {

                                                }

                                                @Override
                                                public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                                    ToastUtil.show(response.getMsg());
                                                }
                                            });
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_send)
    public void sendComment() {
        //登录判断
        if (TextUtils.isEmpty(SystemValue.token)) {
            ActivityUtil.gotoLoginActivity(mActivity);
            return;
        }
        mUpdatesViewModel.sendComment(mActivity, mPostId, editCommentContent.getText().toString());
    }

    @OnClick(R.id.layout_like)
    public void likeClick() {
        if (AppDataUtil.isAlertShow(this)) {
            return;
        }
        if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
            return;
        }
        if (!cbLike.isChecked()) {
            viewLottieZan.playAnimation();
        }
        mUpdatesViewModel.zan(mActivity, mPostId, 0, cbLike.isChecked());
    }
}
