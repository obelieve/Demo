package com.github.obelieve.community.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.community.adapter.CommentReplyListAdapter;
import com.github.obelieve.community.bean.CommentDetailsEntity;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReplyItemBean;
import com.github.obelieve.community.bean.ReplyListEntity;
import com.github.obelieve.event.community.CommentReplyNumNotifyEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zxy.frame.application.BaseApplication;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.base.ApiBaseFragment;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiErrorCode;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.SystemUtil;
import com.zxy.frame.utils.TelephoneUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OneCommentAndAllReplyFragment extends ApiBaseFragment {

    public static final String POST_ID = "post_id";
    public static final String COMMENT_ID = "comment_id";
    public static final String FROM_CID = "from_cid";

    //帖子id
    int mPostId;
    //评论id
    int mCommentId;
    //来自的评论ID 或 回复ID
    int mFromCId;
    ActViewModel mUpdatesViewModel;

    @BindView(R.id.srl_update_detail)
    SmartRefreshLayout srlUpdateDetail;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerUpdateDetail;
    @BindView(R.id.layout_content_detail)
    LinearLayout layoutContentDetail;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    //底部固定评论条
    @BindView(R.id.edit_comment_content)
    EditText editCommentContent;
    @BindView(R.id.layout_like)
    LinearLayout layoutLike;
    @BindView(R.id.cb_like)
    CheckBox cbLike;
    @BindView(R.id.txt_likenum)
    TextView txtLikenum;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.view_lottie_zan)
    LottieAnimationView viewLottieZan;

    CommentReplyListAdapter mCommentReplyListAdapter;
    OnClickReplyContentListener mOnClickReplyContentListener;
    String detailUserName;

    View mViewHeader = null;
    ImageView mIvAvatar;
    TextView mTxt_user_name;
    TextView mTxt_publish_time;
    TextView mTxt_praise;
    TextView mTxt_content;
    LottieAnimationView mHeadeLottieAnimationView;
    //评论Dialog
    Dialog mCommentDialog;
    private int mReplyNum;
    private int mZanNum;

    public static OneCommentAndAllReplyFragment getInstance(int postId, int commentId) {
        OneCommentAndAllReplyFragment fragment = new OneCommentAndAllReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POST_ID, postId);
        bundle.putInt(COMMENT_ID, commentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OneCommentAndAllReplyFragment getMsgInstance(int postId, int commentId, int from_cid) {
        OneCommentAndAllReplyFragment fragment = new OneCommentAndAllReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POST_ID, postId);
        bundle.putInt(COMMENT_ID, commentId);
        bundle.putInt(FROM_CID, from_cid);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int layoutId() {
        return R.layout.fragment_one_comment_and_all_reply;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mPostId = getArguments().getInt(POST_ID, 0);
            mCommentId = getArguments().getInt(COMMENT_ID, 0);
            mFromCId = getArguments().getInt(FROM_CID, 0);
        }
        if (mCommentId == 0) {
            ToastUtil.show("评论不存在");
            return;
        }
        initViewModel();
        mOnClickReplyContentListener = new OnClickReplyContentListener() {
            @Override
            public void onCLick(int to_uid, String to_uname, int to_rid) {
                String hint = "回复:" + to_uname;
                String tag = to_uid + "," + to_uname + "," + to_rid;
                showSendCommentDialog(hint, tag);
            }
        };
        mViewHeader = LayoutInflater.from(mActivity).inflate(R.layout.view_one_comment_and_all_reply_header, null, false);
        mIvAvatar = mViewHeader.findViewById(R.id.img_avatar);
        mTxt_user_name = mViewHeader.findViewById(R.id.txt_user_name);
        mTxt_publish_time = mViewHeader.findViewById(R.id.txt_publish_time);
        mTxt_praise = mViewHeader.findViewById(R.id.txt_praise);
        mTxt_content = mViewHeader.findViewById(R.id.txt_content);
        mHeadeLottieAnimationView = mViewHeader.findViewById(R.id.view_lottie_zan);

        mCommentReplyListAdapter = new CommentReplyListAdapter((AppCompatActivity) mActivity, mOnClickReplyContentListener, mFromCId != 0);
        ((DefaultItemAnimator) recyclerUpdateDetail.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerUpdateDetail.setAdapter(mCommentReplyListAdapter);
        recyclerUpdateDetail.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerUpdateDetail.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if(parent.getAdapter()!=null&& position==parent.getAdapter().getItemCount()-1){
                    outRect.bottom = SystemUtil.dp2px(view.getContext(),25);
                }
            }
        });
        srlUpdateDetail.setEnableLoadMore(false);
        srlUpdateDetail.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mUpdatesViewModel.getComment(mActivity, mCommentId);
                //todo 排序方式
                refreshPage(false);
            }
        });
        srlUpdateDetail.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshPage(true);
            }
        });
        //完成详情获取
        srlUpdateDetail.autoRefresh();
    }

    private void refreshPage(boolean loadMore) {
        mUpdatesViewModel.getReplyList(mActivity, loadMore, mCommentId, mFromCId != 0, mFromCId);
    }


    private void initViewModel() {
        mUpdatesViewModel = ViewModelProviders.of(this).get(ActViewModel.class);
        mUpdatesViewModel.getCommentDetailsEntityMutableLiveData().observe(this, updateDetailEntity1 -> {
            loaderView(updateDetailEntity1);
        });
        mUpdatesViewModel.getGetDataFinish().observe(this, updateDetailEntity -> {
            if (updateDetailEntity.booleanValue()) {
                srlUpdateDetail.finishLoadMore();
            } else {
                srlUpdateDetail.finishRefresh();
            }
        });
        //监听评论回复数据
        mUpdatesViewModel.getReplyListEntityMutableLiveData().observe(this, replyListEntity -> {
            List<ReplyItemBean> commentList = replyListEntity.getData();
            if (replyListEntity.getCurrent_page() <= 1) {
                mCommentReplyListAdapter.getDataHolder().setList(commentList);
            } else {
                mCommentReplyListAdapter.getDataHolder().addAll(commentList);
            }
            //是否有一下页数据
            srlUpdateDetail.setEnableLoadMore(replyListEntity.getCurrent_page() != replyListEntity.getLast_page());
            mCommentReplyListAdapter.notifyDataSetChanged();
        });
        mUpdatesViewModel.getShowdialog().observe(this, aBoolean -> {
            if (aBoolean) {
                if (mActivity instanceof ApiBaseActivity)
                    ((ApiBaseActivity) mActivity).showLoading();
            } else {
                if (mActivity instanceof ApiBaseActivity)
                    ((ApiBaseActivity) mActivity).dismissLoading();
            }
        });
        mUpdatesViewModel.getCommentSuccess().observe(this, aBoolean -> {
            if (mCommentDialog != null) {
                mCommentDialog.dismiss();
            }
            editCommentContent.setText("");
            if (!aBoolean) {
                ToastUtil.show("发布成功");
            }
            srlUpdateDetail.autoRefresh();
        });
        mUpdatesViewModel.getReplySuccess().observe(this, aBoolean -> {
            if (mCommentDialog != null) {
                mCommentDialog.dismiss();
            }
            if (!aBoolean) {
                ToastUtil.show("发布成功");
            }
            srlUpdateDetail.autoRefresh();
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
        mUpdatesViewModel.getLikeOrNo().observe(this, aBoolean -> {
            mZanNum = (aBoolean ? mZanNum + 1 : mZanNum - 1);
            mTxt_praise.setText(mZanNum + "");
            AppDataUtil.showZanComment(mTxt_praise, mZanNum);
            mTxt_praise.setSelected(aBoolean);
            Drawable drawableLeft = null;
            if (aBoolean) {
                drawableLeft = BaseApplication.getContext().getResources().getDrawable(
                        R.drawable.ic_like_selected);
            } else {
                //未赞
                drawableLeft = BaseApplication.getContext().getResources().getDrawable(
                        R.drawable.ic_like_normal);
            }
            mTxt_praise.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
            mTxt_praise.setCompoundDrawablePadding(SystemUtil.dp2px(mTxt_praise.getContext(),6));
            //底部固定评论条
            cbLike.setChecked(aBoolean);
            txtLikenum.setSelected(aBoolean);
            txtLikenum.setText(mZanNum + "");
            AppDataUtil.showZanComment(txtLikenum, mZanNum);
        });
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

    private void showSendCommentDialog(String hint, String tag) {
        if (mCommentDialog != null) {
            mCommentDialog.dismiss();
        }
        mCommentDialog = new Dialog(mActivity, R.style.BottomDialog);
        mCommentDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialog instanceof Dialog) {
                    Dialog d = (Dialog) dialog;
                    if (d.getWindow() != null) {
                        View view = d.getWindow().getDecorView();
                        View clickView = view.findViewById(R.id.btn_ok);
                        if (clickView != null && clickView.getTag() == null) {
                            EditText et = view.findViewById(R.id.edit_comment);
                            if (et != null) {
                                String etTag = null;
                                try {
                                    etTag = (String) et.getTag();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!TextUtils.isEmpty(etTag)) {
                                    String[] strings = etTag.split(",");
                                    if (strings.length == 3) {
                                        int replyId = 0;
                                        try {
                                            replyId = Integer.valueOf(strings[2]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        CacheRepository.getInstance().setPostCommentReply2Reply(mPostId, mCommentId, replyId, et.getText().toString());
                                    }
                                } else {
                                    CacheRepository.getInstance().setPostCommentReply(mPostId, mCommentId, et.getText().toString());
                                }
                            }
                        }
                    }
                }
            }
        });
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.layout_comment_dialog, null);
        EditText editComment = contentView.findViewById(R.id.edit_comment);
        editComment.setHint(hint);
        editComment.setTag(tag);
        if (!TextUtils.isEmpty(tag)) {
            String[] strings = tag.split(",");
            if (strings.length == 3) {
                int replyId = 0;
                try {
                    replyId = Integer.valueOf(strings[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String tempReply = CacheRepository.getInstance().getPostCommentReply2Reply(mPostId, mCommentId, replyId);
                if (!TextUtils.isEmpty(tempReply)) {
                    editComment.setText(tempReply);
                }
            }
        } else {
            String tempComment = CacheRepository.getInstance().getPostCommentReply(mPostId, mCommentId);
            if (!TextUtils.isEmpty(tempComment)) {
                editComment.setText(tempComment);
            }
        }
        contentView.findViewById(R.id.btn_ok).setOnClickListener(view1 ->
        {
            view1.setTag(true);
            if (view1.getContext() instanceof Activity) {
                if (AppDataUtil.isAlertShow((Activity) view1.getContext())) {
                    return;
                }
            }
            if (editComment.getTag() == null) {
                mUpdatesViewModel.sendCommentReply(mActivity, mPostId, mCommentId, editComment.getText().toString());
            } else {
                try {
                    String[] strings = ((String) editComment.getTag()).split(",");
                    if (strings.length == 3) {
                        mUpdatesViewModel.sendCommentReply(mActivity, mPostId, mCommentId, editComment.getText().toString(), Integer.valueOf(strings[0]), strings[1], Integer.valueOf(strings[2]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show("回复错误！");
                }
            }
        });
        mCommentDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = SystemUtil.dp2px(App.getContext(),50);
        contentView.setLayoutParams(layoutParams);
        mCommentDialog.getWindow().setGravity(Gravity.BOTTOM);
        mCommentDialog.show();
        editComment.requestFocus();
        editComment.post(() -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        });
    }

    private void loaderView(CommentDetailsEntity entity) {
        if (entity == null)
            return;
        mPostId = entity.getPost_id();
        mReplyNum = entity.getReply_num();
        EventBus.getDefault().post(CommentReplyNumNotifyEvent.getAllReplyNumInstance(mReplyNum));

        detailUserName = entity.getNickname();
        layoutContentDetail.removeAllViews();
        Glide.with(BaseApplication.getContext())
                .load(entity.getAvatar())
                .placeholder(R.drawable.me_face)
                .error(R.drawable.me_face)
                .into(mIvAvatar);
        mTxt_user_name.setText(entity.getNickname());
        mTxt_publish_time.setText(entity.getComment_time());
        mZanNum = entity.getZan_num();
        mTxt_praise.setText(entity.getZan_num() + "");
        AppDataUtil.showZanComment(mTxt_praise, entity.getZan_num());
        mTxt_praise.setSelected(entity.getIs_zan() == 1);
        mTxt_content.setText(entity.getContent());
        Drawable drawableLeft;
        if (entity.getIs_zan() == 0) {
            //未赞
            drawableLeft = BaseApplication.getContext().getResources().getDrawable(
                    R.drawable.ic_like_normal);
        } else {
            drawableLeft = BaseApplication.getContext().getResources().getDrawable(
                    R.drawable.ic_like_selected);
        }
        mTxt_praise.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        mTxt_praise.setCompoundDrawablePadding(SystemUtil.dp2px(mTxt_praise.getContext(),6));
        mTxt_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getContext() instanceof Activity) {
                    if (AppDataUtil.isAlertShow((Activity) view.getContext())) {
                        return;
                    }
                }
                if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
                    return;
                }
                if (!view.isSelected()) {
                    mHeadeLottieAnimationView.playAnimation();
                }
                mUpdatesViewModel.zan(mActivity, entity.getPost_id(), entity.getComment_id(), view.isSelected());//请求点赞接口
            }
        });
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.gotoPersonalPageActivity(mActivity, entity.getUser_id());
            }
        });
        mTxt_user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.gotoPersonalPageActivity(mActivity, entity.getUser_id());
            }
        });
        //添加评论详情视图
        layoutContentDetail.addView(mViewHeader);
        //添加评论详情头部
        View layout_comment_toolbar = LayoutInflater.from(mActivity).inflate(R.layout.include_one_comment_and_all_reply_toolbar, null, false);

        TextView tv_sort_time = layout_comment_toolbar.findViewById(R.id.tv_sort_time);
        TextView tv_sort_hot = layout_comment_toolbar.findViewById(R.id.tv_sort_hot);
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
                srlUpdateDetail.autoRefresh();
            }
        });
        tv_sort_hot.setOnClickListener(v -> {
            if (!tv_sort_hot.isSelected()) {
                tv_sort_time.setSelected(false);
                tv_sort_hot.setSelected(true);
                mUpdatesViewModel.setCommentSortType("zan_num");
                srlUpdateDetail.autoRefresh();
            }
        });
        layoutContentDetail.addView(layout_comment_toolbar);
        //底部固定评论条
        boolean zan = entity.getIs_zan() == 1;
        cbLike.setChecked(zan);
        txtLikenum.setSelected(zan);
        txtLikenum.setText(mZanNum + "");
        AppDataUtil.showZanComment(txtLikenum, mZanNum);
    }


    @OnClick(R.id.btn_send)
    public void sendComment() {
        //登录判断
        if (TextUtils.isEmpty(SystemValue.token)) {
            ActivityUtil.gotoLoginActivity(mActivity);
            return;
        }
        mUpdatesViewModel.sendCommentReply(mActivity, mPostId, mCommentId, editCommentContent.getText().toString());
    }

    @OnClick(R.id.layout_like)
    public void likeClick() {
        if (AppDataUtil.isAlertShow(mActivity)) {
            return;
        }
        if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
            return;
        }
        if (!cbLike.isChecked()) {
            viewLottieZan.playAnimation();
        }
        mUpdatesViewModel.zan(mActivity, mPostId, mCommentId, cbLike.isChecked());//请求点赞接口
    }

    /**
     * 点击回复内容监听
     */
    public interface OnClickReplyContentListener {
        void onCLick(int to_uid, String to_uname, int to_rid);
    }

    public static class ActViewModel extends ViewModel {

        //评论详情数据
        private MutableLiveData<CommentDetailsEntity> commentDetailsEntityMutableLiveData = new MutableLiveData<>();
        //评论回复列表数据
        private MutableLiveData<ReplyListEntity> replyListEntityMutableLiveData = new MutableLiveData<>();

        //获取数据结束 true加载更多，false下拉刷新
        private MutableLiveData<Boolean> getDataFinish = new MutableLiveData<>();

        //是否加载
        private MutableLiveData<Boolean> showdialog = new MutableLiveData<>();

        //评论发布成功
        private MutableLiveData<Boolean> commentSuccess = new MutableLiveData<>();

        //对回复发布成功
        private MutableLiveData<Boolean> replySuccess = new MutableLiveData<>();

        //点赞成功 是否取消
        private MutableLiveData<Boolean> likeOrNo = new MutableLiveData<>();

        int mCommentPage = 0;
        String commentSortType = "created_at";

        /**
         * 评论详情
         * Path
         * /api/get_comment
         * <p>
         * comment_id	int	是	评论ID
         */
        public void getComment(Activity activity, int comment_id) {
            ApiService.wrap(App.getServiceInterface().getComment(comment_id), CommentDetailsEntity.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<CommentDetailsEntity>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            Log.e("RespondThrowable", e.message);
                            getDataFinish.postValue(false);
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<CommentDetailsEntity> response, boolean isProcessed) {
                            CommentDetailsEntity data = response.getEntity();
                            commentDetailsEntityMutableLiveData.postValue(data);
                            getDataFinish.postValue(false);
                        }
                    });
        }

        /**
         * 评论回复列表
         */
        public void getReplyList(Activity activity, boolean isMore, int comment_id, boolean msgEntry, int from_cid) {
            if (!isMore) {
                mCommentPage = 0;
            }
            ApiService.wrap(App.getServiceInterface().getReplyList(comment_id, mCommentPage, commentSortType, msgEntry ? from_cid : null), ReplyListEntity.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<ReplyListEntity>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            Log.e("RespondThrowable", e.message);
                            //page>1代表是获取更多评论，则关闭页面的上拉加载更多
                            if (mCommentPage > 1) {
                                getDataFinish.postValue(true);
                            } else {
                                getDataFinish.postValue(false);
                            }
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<ReplyListEntity> response, boolean isProcessed) {
                            ReplyListEntity data = response.getEntity();
                            replyListEntityMutableLiveData.setValue(data);
                            //page>1代表是获取更多评论，则关闭页面的上拉加载更多
                            if (mCommentPage > 1) {
                                getDataFinish.postValue(true);
                            } else {
                                getDataFinish.postValue(false);
                            }
                            mCommentPage = data.getCurrent_page() + 1;
                        }
                    });
        }

        /**
         * 评论回复
         *
         * @param post_id
         * @param comment_id
         * @param content
         */
        public void sendCommentReply(Activity activity, int post_id, int comment_id, String content) {
            showdialog.postValue(true);
            ApiService.wrap(App.getServiceInterface().sendPostComment(post_id, content, comment_id), String.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            CacheRepository.getInstance().setPostCommentReply(post_id, comment_id, content);
                            showdialog.postValue(false);
                            if (e.code == ApiErrorCode.CODE_NOSET_NICKNAME) {
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
                            CacheRepository.getInstance().setPostCommentReply(post_id, comment_id, "");
                            showdialog.postValue(false);
                            commentSuccess.postValue(isProcessed);
                        }
                    });
        }

        /**
         * 对评论的回复进行回复
         *
         * @param post_id
         * @param comment_id
         * @param content
         */
        public void sendCommentReply(Activity activity, int post_id, int comment_id, String content, int to_uid, String to_uname, int to_rid) {
            showdialog.postValue(true);
            ApiService.wrap(App.getServiceInterface().sendPostComment(post_id, content, comment_id, to_uid, to_uname, to_rid),String.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            CacheRepository.getInstance().setPostCommentReply2Reply(post_id, comment_id, to_rid, content);
                            showdialog.postValue(false);
                            ToastUtil.show(e.message);
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                            CacheRepository.getInstance().setPostCommentReply2Reply(post_id, comment_id, to_rid, "");
                            showdialog.postValue(false);
                            replySuccess.postValue(isProcessed);
                        }
                    });
        }

        /**
         * 点赞
         *
         * @param postId
         * @param commentId
         */
        public void zan(Activity activity, int postId, int commentId, boolean like) {
            if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
                return;
            }
            likeOrNo.setValue(!like);
            ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(postId, commentId != 0 ? commentId : null), PraiseEntity.class)
                    .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>(activity) {
                        @Override
                        public void onError(ApiServiceException e) {
                            Log.e("RespondThrowable", e.message);
                        }

                        @Override
                        public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                        }
                    });
        }

        public MutableLiveData<CommentDetailsEntity> getCommentDetailsEntityMutableLiveData() {
            return commentDetailsEntityMutableLiveData;
        }

        public MutableLiveData<ReplyListEntity> getReplyListEntityMutableLiveData() {
            return replyListEntityMutableLiveData;
        }

        public MutableLiveData<Boolean> getGetDataFinish() {
            return getDataFinish;
        }

        public MutableLiveData<Boolean> getShowdialog() {
            return showdialog;
        }

        public MutableLiveData<Boolean> getCommentSuccess() {
            return commentSuccess;
        }

        public MutableLiveData<Boolean> getReplySuccess() {
            return replySuccess;
        }

        public MutableLiveData<Boolean> getLikeOrNo() {
            return likeOrNo;
        }

        public String getCommentSortType() {
            return commentSortType;
        }

        public void setCommentSortType(String commentSortType) {
            this.commentSortType = commentSortType;
        }
    }
}
