package com.github.obelieve.community.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.event.community.PostFilterCacheEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.thirdsdklib.ImagePreviewUtil;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.github.obelieve.utils.BottomMenuUtil;
import com.github.obelieve.utils.others.TranslationState;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.ApiBaseActivity;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.SystemInfoUtil;
import com.zxy.frame.utils.TelephoneUtil;
import com.zxy.frame.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin
 * on 2020/8/23
 */
public class UpdatesAdapter2 extends BaseRecyclerViewAdapter<SquareListsEntity.PostListBean> implements AppDataUtil.OnItemClickPopupMenuListener {

    private String mTag;

    public UpdatesAdapter2(Activity context, String tag) {
        super(context);
        this.mTag = tag;
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new UpdatesViewHolder2(parent);
    }

    @Override
    public void loadViewHolder(BaseViewHolder holder, int position) {
        holder.bind(getDataHolder().getList().get(position));
    }

    @Override
    public void onItemClickCopy(int position) {
        Toast.makeText(getContext(), "已复制", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickHideTranslation(int position) {

    }

    @Override
    public void onItemClickCollection(int position) {
        Toast.makeText(getContext(), "已收藏", Toast.LENGTH_SHORT).show();
    }

    public class UpdatesViewHolder2 extends BaseViewHolder<SquareListsEntity.PostListBean> {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.civ_avatar)
        CircleImageView civAvatar;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_level)
        ImageView ivLevel;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.cb_like)
        CheckBox cbLike;
        @BindView(R.id.tv_like)
        TextView tvLike;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.cl_bottom)
        ConstraintLayout clBottom;
        @BindView(R.id.view_lottie_like)
        LottieAnimationView view_lottie_like;
        @BindView(R.id.iv_gif)
        ImageView ivGif;

        private Context mContext;
        private DelPostDialog mDelPostDialog;
        private SquareListsEntity.PostListBean mPostListBean;

        public UpdatesViewHolder2(ViewGroup parent) {
            super(parent, R.layout.viewholder_updates2);
            mContext = parent.getContext();
        }

        @Override
        public void bind(SquareListsEntity.PostListBean bean) {
            mPostListBean = bean;
            if (bean.getMedia() != null && bean.getMedia().getMedia_list() != null && bean.getMedia().getMedia_list().size() > 0 && bean.getMedia().getMedia_list().get(0) != null) {
                String path = bean.getMedia().getMedia_list().get(0).getThumbnail();
                path = path == null ? "" : path;
                Glide.with(mContext).load(path).placeholder(R.drawable.failed_to_load_2).error(R.drawable.failed_to_load_2).into(ivImage);
                if (path.contains(".gif")) {
                    ivGif.setVisibility(View.VISIBLE);
                } else {
                    ivGif.setVisibility(View.GONE);
                }
            }
            tvContent.setText(bean.getContent());
            tvContent.setVisibility(bean.getContent().length() == 0 ? View.GONE : View.VISIBLE);
            Glide.with(mContext).load(bean.getAvatar()).placeholder(R.drawable.me_face).error(R.drawable.me_face).into(civAvatar);
            tvName.setText(bean.getNickname());
//            ivLevel.setImageResource(AppDataUtil.getLevelSmallResource(bean.getUser_level()));
            tvComment.setText(bean.getPc_num() + "");
            tvLike.setText(bean.getZan_num() + "");
            tvLike.setSelected(bean.getIs_zan() == 1);
            cbLike.setChecked(bean.getIs_zan() == 1);
            AppDataUtil.showZanComment(tvLike, bean.getZan_num());
            AppDataUtil.showZanComment(tvComment, bean.getPc_num());
            ivMore.setVisibility(View.VISIBLE);

        }

        @OnLongClick({R.id.tv_content})
        public void onViewLongClicked(View view) {
            if (mPostListBean != null) {
                int position = getDataHolder().getList().indexOf(mPostListBean);
                if (position != -1) {
                    AppDataUtil.showPopupMenu(mContext, UpdatesAdapter2.this, position, view, TranslationState.START);
                }
            }
        }

        @OnClick({R.id.cl_content, R.id.iv_image, R.id.tv_content, R.id.ll_content, R.id.ll_like, R.id.ll_comment, R.id.iv_more})
        public void onViewClicked(View view) {
            if (mPostListBean == null) return;
            switch (view.getId()) {
                case R.id.iv_image:
                    if (mContext instanceof Activity) {
                        List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list = mPostListBean.getMedia().getMedia_list();
                        List<View> views = new ArrayList<>();
                        List<String> imgUrls = new ArrayList<>();
                        views.add(view);
                        for (int i = 0; i < media_list.size(); i++) {
                            if (media_list.get(i) != null) {
                                imgUrls.add(media_list.get(i).getOriginal());
                            }
                        }
                        ImagePreviewUtil.show((Activity) mContext, views, imgUrls, 0);
                    }
                    break;
                case R.id.cl_content:
                case R.id.tv_content:
                    ActivityUtil.gotoUpdateDetailActivity(mContext, mPostListBean.getPost_id(), mPostListBean.getUser_id(), mTag);
                    if (mItemClickCallback != null) {
                        int position = getDataHolder().getList().indexOf(mPostListBean);
                        if (position != -1) {
                            mItemClickCallback.onItemClick(itemView, mPostListBean, position);
                        }
                    }
                    break;
                case R.id.ll_content:
                    ActivityUtil.gotoPersonalPageActivity(mContext, mPostListBean.getUser_id());
                    break;
                case R.id.ll_like:
                    if (view.getContext() instanceof Activity) {
                        if (AppDataUtil.isAlertShow((Activity) view.getContext())) {
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(mContext);
                        return;
                    }
                    int position = getDataHolder().getList().indexOf(mPostListBean);
                    if (position != -1) {
                        likeOrNoComment(view_lottie_like, position, mPostListBean);
                    }
                    break;
                case R.id.ll_comment:
                    if (view.getContext() instanceof Activity) {
                        if (AppDataUtil.isAlertShow((Activity) view.getContext())) {
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(SystemValue.token)) {
                        ActivityUtil.gotoLoginActivity(mContext);
                    } else {
                        Dialog bottomDialog = new Dialog(mContext, R.style.BottomDialog);
                        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_dialog, null);
                        EditText editComment = contentView.findViewById(R.id.edit_comment);
                        contentView.findViewById(R.id.btn_ok).setOnClickListener(view1 -> sendComment(bottomDialog, mPostListBean, editComment.getText().toString()));
                        bottomDialog.setContentView(contentView);
                        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                        layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
                        layoutParams.height = SystemInfoUtil.dp2px(App.getContext(),50);
                        contentView.setLayoutParams(layoutParams);
                        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
                        bottomDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                        bottomDialog.show();
                        editComment.requestFocus();
                        editComment.postDelayed(() -> {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(editComment, 0);
                        }, 50);
                    }
                    break;
                case R.id.iv_more:
                    showDeleteOrReport(mPostListBean.getUser_id(), mPostListBean.getPost_id(), "post");
                    break;
            }
        }

        private void sendComment(Dialog dialog, SquareListsEntity.PostListBean postListBean, String content) {
            if (mContext instanceof Activity) {
                ((ApiBaseActivity) mContext).showLoading();
                ApiService.wrap(App.getServiceInterface().sendPostComment(postListBean.getPost_id(), content), String.class)
                        .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>((Activity) mContext) {
                            @Override
                            public void onError(ApiServiceException e) {
                                ((ApiBaseActivity) mContext).dismissLoading();
                                ToastUtil.show(e.message);
                            }

                            @Override
                            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                ((ApiBaseActivity) mContext).dismissLoading();
                                dialog.dismiss();
                                postListBean.setPc_num(postListBean.getPc_num() + 1);
                                if (!isProcessed) {
                                    ToastUtil.show("发布成功");
                                }
                                notifyDataSetChanged();
                            }
                        });
            }
        }

        private void likeOrNoComment(LottieAnimationView view, int position, SquareListsEntity.PostListBean postListBean) {
            if (!TelephoneUtil.isNetworkAvailable(App.getContext())) {
                return;
            }
            postListBean.setIs_zan(postListBean.getIs_zan() == 0 ? 1 : 0);
            if (postListBean.getIs_zan() == 1) {
                view.playAnimation();
            }
            postListBean.setZan_num(postListBean.getIs_zan() == 1 ? postListBean.getZan_num() + 1 : postListBean.getZan_num() - 1);
            if (position >= 0 && position < getItemCount()) {
                notifyItemChanged(position);
            }
            if (mContext instanceof Activity) {
                ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(postListBean.getPost_id(), null), PraiseEntity.class)
                        .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>((Activity) mContext) {
                            @Override
                            public void onError(ApiServiceException e) {
                            }

                            @Override
                            public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                            }
                        });
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
            if (mContext == null) {
                return;
            }
            UserEntity userEntity = CacheRepository.getInstance().getUserEntity();
            int curUserId = userEntity != null ? userEntity.user_id : 0;
            boolean isCurUser = curUserId == userId;
            String[] items = null;
            if (isCurUser) {
                items = new String[]{"删除", "取消"};
            } else {
                items = new String[]{"举报", "不感兴趣", "屏蔽此人", "取消"};
            }
            BottomMenuUtil.show((AppCompatActivity) mContext, items, (text, index) -> {
                //返回参数 text 即菜单名称，index 即菜单索引
                switch (index) {
                    case 0:
                        if (isCurUser) {
                            showDelDialog(p_id, p_type);
                        } else {
                            if (TextUtils.isEmpty(SystemValue.token)) {
                                ActivityUtil.gotoLoginActivity(mContext);
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
                                ActivityUtil.gotoLoginActivity(mContext);
                                return;
                            }
                            PostFilterCacheEvent cache = new PostFilterCacheEvent(mTag, p_id, 0);
                            CacheRepository.getInstance().addPostIdFromPostFilter(p_id);
                            EventBus.getDefault().post(cache);
                            ToastUtil.show("我们将减少这方面内容的推送");
                        }
                        break;
                    case 2:
                        if (TextUtils.isEmpty(SystemValue.token)) {
                            ActivityUtil.gotoLoginActivity(mContext);
                            return;
                        }
                        PostFilterCacheEvent cache2 = new PostFilterCacheEvent(mTag, 0, userId);
                        CacheRepository.getInstance().addUserIdPostFilter(userId);
                        EventBus.getDefault().post(cache2);
                        ToastUtil.show("将为你过滤此用户所发的内容");
                        break;
                }
            });
        }

        private void showDelDialog(int p_id, String p_type) {
            if (mContext == null)
                return;
            if (mDelPostDialog == null && mContext instanceof Activity) {
                mDelPostDialog = new DelPostDialog((Activity) mContext);
            } else {
                mDelPostDialog.dismiss();
            }
            mDelPostDialog.setData(mTag, p_id, p_type);
            mDelPostDialog.show();
        }

        /**
         * 举报
         *
         * @param p_id   帖子ID\评论ID\回复ID
         * @param p_type 举报类型 post-帖子；comment-评论；reply-回复
         */
        private void showReport(int p_id, String p_type) {
            if (mContext instanceof ApiBaseActivity) {
                ((ApiBaseActivity) mContext).showLoading();
            }
            if (mContext instanceof Activity) {
                ApiService.wrap(App.getServiceInterface().getReportType(), ReportTypeEntity.class)
                        .subscribe(new ApiBaseSubscribe<ApiBaseResponse<ReportTypeEntity>>((Activity) mContext) {
                            @Override
                            public void onError(ApiServiceException e) {
                                if (mContext instanceof ApiBaseActivity) {
                                    ((ApiBaseActivity) mContext).dismissLoading();
                                }
                            }

                            @Override
                            public void onSuccess(ApiBaseResponse<ReportTypeEntity> response, boolean isProcessed) {
                                if (mContext == null)
                                    return;
                                if (mContext instanceof ApiBaseActivity) {
                                    ((ApiBaseActivity) mContext).dismissLoading();
                                }
                                try {
                                    List<ReportTypeEntity.ReportTypeBean> list = response.getEntity().getReport_type();
                                    int[] ids = new int[list.size()];
                                    String[] names = new String[list.size() + 1];
                                    for (int i = 0; i < list.size(); i++) {
                                        ids[i] = list.get(i).getRt_id();
                                        names[i] = list.get(i).getRt_name();
                                    }
                                    names[list.size()] = "取消";
                                    BottomMenuUtil.show((AppCompatActivity) mContext, names, (text2, index2) -> {
                                        if (index2 != list.size()) {
                                            if (mContext instanceof Activity) {
                                                ApiService.wrap(App.getServiceInterface().reportPost(p_id, ids[index2], p_type), String.class)
                                                        .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>() {
                                                            @Override
                                                            public void onError(ApiServiceException e) {

                                                            }

                                                            @Override
                                                            public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                                                ToastUtil.show(response.getMsg());
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }
    }

    public static class DelPostDialog extends CommonDialog {

        private Activity mContext;
        private String mTag;
        private int mP_id;
        private String mP_type;

        public DelPostDialog(Activity activity) {
            super(activity);
            init(activity);
        }

        public void init(Activity context) {
            mContext = context;
            setContent("是否确认要删除？");
            setNegativeButton("取消", new CommonDialog.onCancelListener() {
                @Override
                public void onClickCancel(Dialog dialog) {
                    dialog.dismiss();
                }
            });
            setPositiveButton("确定", new CommonDialog.onSubmitListener() {
                @Override
                public void onClickSubmit(Dialog dialog) {
                    dialog.dismiss();
                    ApiService.wrap(App.getServiceInterface().delPost(mP_id, mP_type), String.class)
                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mContext) {
                                @Override
                                public void onError(ApiServiceException e) {
                                    ToastUtil.show("删除失败！");
                                }

                                @Override
                                public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
                                    ToastUtil.show(response.getMsg());
                                    EventBus.getDefault().post(new PostFilterCacheEvent(mTag, mP_id, 0));
                                }
                            });
                }
            });
        }

        public void setData(String tag, int p_id, String p_type) {
            mTag = tag;
            mP_id = p_id;
            mP_type = p_type;
        }
    }
}
