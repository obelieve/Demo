package com.github.obelieve.community.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.CommentListEntity;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.viewmodel.SendCommentModule;
import com.github.obelieve.event.community.PostCommentDelRefreshEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.github.obelieve.utils.BottomMenuUtil;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.github.obelieve.ui.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.info.TelephoneUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends BaseRecyclerViewAdapter<CommentListEntity.DataBean> {

    AppCompatActivity context;
    SendCommentModule mSendCommentModule;
    CommonDialog mAlertDialog;
    boolean mFirstLight;

    public CommentListAdapter(AppCompatActivity context, SendCommentModule module, boolean firstLight) {
        super(context);
        this.context = context;
        this.mSendCommentModule = module;
        this.mFirstLight = firstLight;
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new CommentItemViewHolder(parent);
    }

    @Override
    public void loadViewHolder(BaseViewHolder holder, int position) {
        CommentItemViewHolder vh = (CommentItemViewHolder) holder;
        if (mFirstLight && position == 0) {
            vh.txtContent.setSelected(false);
        } else {
            vh.txtContent.setSelected(false);
        }
        holder.bind(getDataHolder().getList().get(position));
    }

    /**
     * 显示举报或删除
     *
     * @param dataBean
     * @param userId
     * @param p_id     帖子ID\评论ID\回复ID
     * @param p_type   举报类型 post-帖子；comment-评论；reply-回复
     */
    private void showDeleteOrReport(CommentListEntity.DataBean dataBean, int userId, int p_id, String p_type, CommentListEntity.DataBean.ReplyListBean replyListBean) {
        UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
        int curUserId = userEntity != null ? userEntity.user_id : 0;
        boolean isCurUser = curUserId == userId;
        String[] items = null;
        if (isCurUser) {
            items = new String[]{"删除", "取消"};
        } else {
            items = new String[]{"举报", "取消"};
        }
        BottomMenuUtil.show(context, items, (text, index) -> {
            //返回参数 text 即菜单名称，index 即菜单索引
            if (isCurUser) {
                switch (index) {
                    case 0:
                        showDelDialog(dataBean, p_id, p_type, replyListBean);//删除
                        break;
                    case 1:
                        break;
                }
            } else {
                switch (index) {
                    case 0:
                        showReport(p_id, p_type);//举报
                        break;
                    case 1:
                        break;
                }
            }
        });
    }

    /**
     * 删除评论弹框
     *
     * @param dataBean
     * @param p_id
     * @param p_type
     */
    private void showDelDialog(final CommentListEntity.DataBean dataBean, final int p_id, final String p_type, CommentListEntity.DataBean.ReplyListBean replyListBean) {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        mAlertDialog = new CommonDialog(context);
        mAlertDialog.setNegativeButton("取消", new CommonDialog.onCancelListener() {
            @Override
            public void onClickCancel(Dialog dialog) {
                dialog.dismiss();
            }
        });
        String content = (p_type != null && p_type.equals("comment")) ? "评论" : "回复";
        mAlertDialog.setContent("确定是否删除该" + content + "？");
        mAlertDialog.setPositiveButton("确定", new CommonDialog.onSubmitListener() {
            @Override
            public void onClickSubmit(Dialog dialog) {
                dialog.dismiss();
                ApiService.wrap(App.getServiceInterface().delPost(p_id, p_type), String.class)
                        .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(context) {
                            @Override
                            public void onError(ApiServiceException e) {
                                ToastUtil.show("删除失败！");
                            }

                            @Override
                            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                ToastUtil.show(response.getMsg());
                                if (p_type != null) {
                                    if (p_type.equals("comment")) {
                                        EventBus.getDefault().post(new PostCommentDelRefreshEvent());
                                        getDataHolder().getList().remove(dataBean);
                                        notifyDataSetChanged();
                                    } else if (p_type.equals("reply") && dataBean.getReply_list() != null && replyListBean != null) {
                                        dataBean.getReply_list().remove(replyListBean);
                                        dataBean.setReply_num(dataBean.getReply_num() - 1);
                                        notifyDataSetChanged();
                                        EventBus.getDefault().post(new PostCommentDelRefreshEvent());
                                    }
                                }
                            }
                        });
            }
        });
        mAlertDialog.show();
    }

    /**
     * 举报
     *
     * @param p_id   帖子ID\评论ID\回复ID
     * @param p_type 举报类型 post-帖子；comment-评论；reply-回复
     */
    private void showReport(int p_id, String p_type) {
        ApiService.wrap(App.getServiceInterface().getReportType(), ReportTypeEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<ReportTypeEntity>>(context) {
                    @Override
                    public void onError(ApiServiceException e) {

                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<ReportTypeEntity> response, boolean isProcessed) {
                        try {
                            List<ReportTypeEntity.ReportTypeBean> list = response.getEntity().getReport_type();
                            int[] ids = new int[list.size()];
                            String[] names = new String[list.size() + 1];
                            for (int i = 0; i < list.size(); i++) {
                                ids[i] = list.get(i).getRt_id();
                                names[i] = list.get(i).getRt_name();
                            }
                            names[list.size()] = "取消";
                            BottomMenuUtil.show(context, names, (text2, index2) -> {
                                if (index2 != list.size()) {
                                    ApiService.wrap(App.getServiceInterface().reportPost(p_id, ids[index2], p_type), String.class)
                                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(context) {
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

    class CommentItemViewHolder extends BaseViewHolder<CommentListEntity.DataBean> {

        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.ll_name)
        LinearLayout llName;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.cb_like)
        CheckBox cbLike;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.txt_state)
        TextView txtState;
        @BindView(R.id.fl_content)
        FrameLayout flContent;
        @BindView(R.id.txt_publish_time)
        TextView txtPublishTime;
        @BindView(R.id.tv_reply)
        TextView tvReply;
        @BindView(R.id.ll_time)
        LinearLayout llTime;
        @BindView(R.id.txt_reply)
        TextView txtReply;
        @BindView(R.id.txt_replyto)
        TextView txtReplyto;
        @BindView(R.id.txt_comment_num)
        TextView txtCommentNum;
        @BindView(R.id.view_avatar)
        View viewAvatar;
        @BindView(R.id.fl_comment_context)
        FrameLayout flCommentContext;
        @BindView(R.id.view_lottie_zan)
        public LottieAnimationView viewLottieZan;

        CommentListEntity.DataBean mData;

        public CommentItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_recycler_comment);
        }

        @Override
        public void bind(CommentListEntity.DataBean data) {
            mData = data;
            Glide.with(context).load(data.getAvatar()).placeholder(R.drawable.me_face).error(R.drawable.me_face).into(civAvatar);
            txtContent.setText(data.getContent());
            tvName.setText(data.getNickname());
//            ivLevel.setImageResource(AppDataUtil.getLevelSmallResource(data.getUser_level()));
            txtPublishTime.setText(data.getComment_time());
            tvLike.setText(data.getZan_num() + "");
            llLike.setSelected(data.getIs_zan() == 1);
            AppDataUtil.showZanComment(tvLike, data.getZan_num());
            cbLike.setChecked(data.getIs_zan() == 1);
            if (data.getReply_list().size() > 0) {
                txtReply.setVisibility(View.VISIBLE);
                CommentListEntity.DataBean.ReplyListBean replyListBean = data.getReply_list().get(0);
                txtReply.setText(creatSpannableReplyString(replyListBean.getNickname(), replyListBean.getContent()));
                if (data.getReply_list().size() > 1) {
                    txtReplyto.setVisibility(View.VISIBLE);
                    CommentListEntity.DataBean.ReplyListBean replyListBeanTo = data.getReply_list().get(1);
                    if (replyListBeanTo.getTo_uid() == 0) {
                        txtReplyto.setText(creatSpannableReplyString(replyListBeanTo.getNickname(), replyListBeanTo.getContent()));
                    } else {
                        txtReplyto.setText(creatSpannableReplyString(replyListBeanTo.getNickname(), replyListBeanTo.getTo_uname(), replyListBeanTo.getContent()));
                    }
                } else {
                    txtReplyto.setVisibility(View.GONE);
                }
            } else {
                txtReply.setVisibility(View.GONE);
            }
            if (data.getReply_num() > 2) {
                AppDataUtil.showZanComment(txtCommentNum, "共%s条回复>", data.getReply_num());
                txtCommentNum.setVisibility(View.VISIBLE);
            } else {
                txtCommentNum.setVisibility(View.GONE);
            }
            if (data.getReply_list().size() > 0 || data.getReply_num() > 2) {
                flCommentContext.setVisibility(View.VISIBLE);
                flCommentContext.setOnClickListener(view -> ActivityUtil.gotoOneCommentAndAllReplyActivity(context, data.getPost_id(), data.getComment_id()));
            } else {
                flCommentContext.setVisibility(View.GONE);
                flCommentContext.setOnClickListener(null);
            }
        }

        @OnClick({R.id.view_avatar, R.id.ll_name, R.id.iv_more, R.id.ll_like, R.id.tv_reply})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.view_avatar:
                case R.id.ll_name:
                    if (mData != null) {
                        ActivityUtil.gotoPersonalPageActivity(context, mData.getUser_id());
                    }
                    break;
                case R.id.iv_more:
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(context);
                        return;
                    }
                    if (mData != null) {
                        showDeleteOrReport(mData, mData.getUser_id(), mData.getComment_id(), "comment", null);
                    }
                    break;
                case R.id.ll_like:
                    if (view.getContext() instanceof Activity) {
                        if (AppDataUtil.isAlertShow((Activity) view.getContext())) {
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(context);
                        return;
                    }
                    if (mData != null) {
                        int position = getDataHolder().getList().indexOf(mData);
                        if (position >= 0 && position < getDataHolder().getList().size()) {
                            likeOrNoComment(viewLottieZan, position, mData);
                        }
                    }
                    break;
                case R.id.tv_reply:
                    if (AppDataUtil.isAlertShow(context)) {
                        return;
                    }
                    if (mData != null) {
                        mSendCommentModule.setCommentId(mData.getComment_id());
                        mSendCommentModule.showSendCommentDialog("回复:" + mData.getNickname(), null);
                    }
                    break;
            }
        }
    }

    private SpannableStringBuilder creatSpannableReplyString(String userName, String content) {
        /**
         * 使用SpannableString设置样式——字体颜色
         */
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(userName);
        spannableString.append(":");
        spannableString.append(content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.comment_text_blue_color));
        spannableString.setSpan(colorSpan, 0, userName.length() + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    private SpannableStringBuilder creatSpannableReplyString(String userName, String toUserNamse, String content) {
        /**
         * 使用SpannableString设置样式——字体颜色
         */
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(userName);
        spannableString.append("回复");
        spannableString.append(toUserNamse);
        spannableString.append(":");
        spannableString.append(content);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.comment_text_blue_color));
        spannableString.setSpan(colorSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan replySpan = new ForegroundColorSpan(context.getResources().getColor(R.color.comment_reply_text_color));
        spannableString.setSpan(replySpan, userName.length(), userName.length() + 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorhuifuSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.comment_text_blue_color));
        spannableString.setSpan(colorhuifuSpan, userName.length() + 2, userName.length() + 3 + toUserNamse.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    private void likeOrNoComment(LottieAnimationView view, int position, CommentListEntity.DataBean dataBean) {
        if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
            return;
        }
        dataBean.setIs_zan(dataBean.getIs_zan() == 0 ? 1 : 0);
        if (dataBean.getIs_zan() == 1) {
            view.playAnimation();
        }
        dataBean.setZan_num(dataBean.getIs_zan() == 1 ? dataBean.getZan_num() + 1 : dataBean.getZan_num() - 1);
        int index = getDataHolder().getList().indexOf(dataBean);
        if (index != -1 && index < getDataHolder().getList().size()) {
            notifyItemChanged(index);
        }
        ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(dataBean.getPost_id(), dataBean.getComment_id() != 0 ? dataBean.getComment_id() : null), PraiseEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>(context) {
                    @Override
                    public void onError(ApiServiceException e) {
                        Log.e("RespondThrowable", e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                    }
                });
    }
}
