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
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReplyItemBean;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.ui.OneCommentAndAllReplyFragment;
import com.github.obelieve.event.community.CommentReplyNumNotifyEvent;
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

/**
 * Created by Admin
 * on 2020/8/25
 */
public class CommentReplyListAdapter extends BaseRecyclerViewAdapter<ReplyItemBean> {

    OneCommentAndAllReplyFragment.OnClickReplyContentListener mOnClickReplyContentListener;
    AppCompatActivity context;
    CommonDialog mAlertDialog;
    boolean mFirstLight;

    public CommentReplyListAdapter(AppCompatActivity context, OneCommentAndAllReplyFragment.OnClickReplyContentListener listener, boolean firstLight) {
        super(context);
        this.context = context;
        this.mOnClickReplyContentListener = listener;
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
    private void showDeleteOrReport(ReplyItemBean dataBean, int userId, int p_id, String p_type) {
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
                        showDelDialog(dataBean, p_id, p_type);//删除
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
     * 删除评论回复弹框
     *
     * @param dataBean
     * @param p_id
     * @param p_type
     */
    private void showDelDialog(final ReplyItemBean dataBean, final int p_id, final String p_type) {
        if (mAlertDialog == null) {
            mAlertDialog = new CommonDialog(context);
            mAlertDialog.setContent("确定是否删除该回复？");
            mAlertDialog.setNegativeButton("取消", new CommonDialog.onCancelListener() {
                @Override
                public void onClickCancel(Dialog dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            mAlertDialog.dismiss();
        }
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
                                getDataHolder().getList().remove(dataBean);
                                EventBus.getDefault().post(CommentReplyNumNotifyEvent.getReplyRemoveInstance());
                                notifyDataSetChanged();
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

    public class CommentItemViewHolder extends BaseViewHolder<ReplyItemBean> {
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
        @BindView(R.id.view_avatar)
        View viewAvatar;
        @BindView(R.id.view_lottie_zan)
        LottieAnimationView viewLottieZan;

        private ReplyItemBean mData;
        public CommentItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_recycler_comment_reply);
        }

        @Override
        public void bind(ReplyItemBean data) {
            mData = data;
            Glide.with(context).load(data.getAvatar()).placeholder(R.drawable.me_face).error(R.drawable.me_face).into(civAvatar);
//            ivLevel.setImageResource(AppDataUtil.getLevelSmallResource(data.getUser_level()));
            tvName.setText(data.getNickname());
            txtPublishTime.setText(data.getReply_time());
            tvLike.setText(data.getZan_num() + "");
            llLike.setSelected(data.getIs_zan() == 1);
            AppDataUtil.showZanComment(tvLike, data.getZan_num());
            cbLike.setChecked(data.getIs_zan() == 1);
            if (data.getTo_uid() == 0) {
                txtContent.setText(data.getContent());
            } else {
                txtContent.setText(creatSpannableReplyString("", data.getTo_uname(), data.getContent()));
            }
        }

        @OnClick({R.id.view_avatar, R.id.ll_name, R.id.iv_more, R.id.ll_like, R.id.tv_reply})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.ll_name:
                case R.id.view_avatar:
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
                        showDeleteOrReport(mData, mData.getUser_id(), mData.getReply_id(), "reply");
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
                        mOnClickReplyContentListener.onCLick(mData.getUser_id(), mData.getNickname(), mData.getReply_id());
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
        spannableString.setSpan(colorSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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
        spannableString.setSpan(colorSpan, userName.length() + 2, userName.length() + 3 + toUserNamse.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    private void likeOrNoComment(LottieAnimationView view, int position, ReplyItemBean dataBean) {
        if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
            return;
        }
        dataBean.setIs_zan(dataBean.getIs_zan() == 0 ? 1 : 0);
        if (dataBean.getIs_zan() == 1) {
            view.playAnimation();
        }
        dataBean.setZan_num(dataBean.getIs_zan() == 1 ? dataBean.getZan_num() + 1 : dataBean.getZan_num() - 1);
        if (position >= 0 && position < getItemCount()) {
            notifyItemChanged(position);
        }
        ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(dataBean.getPost_id(), dataBean.getReply_id() != 0 ? dataBean.getReply_id() : null), PraiseEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>(context) {
                    @Override
                    public void onError(ApiServiceException e) {
                        Log.e("RespondThrowable", ""+e.getMessage());
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                    }
                });
    }
}
