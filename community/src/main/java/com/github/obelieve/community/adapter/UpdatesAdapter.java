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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.obelieve.App;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.PraiseEntity;
import com.github.obelieve.community.bean.ReportTypeEntity;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.ui.view.NineGridView;
import com.github.obelieve.event.community.PostFilterCacheEvent;
import com.github.obelieve.login.entity.UserEntity;
import com.github.obelieve.net.ApiServiceWrapper;
import com.github.obelieve.repository.CacheRepository;
import com.github.obelieve.repository.cache.constant.SystemValue;
import com.github.obelieve.utils.ActivityUtil;
import com.github.obelieve.utils.AppDataUtil;
import com.github.obelieve.utils.BottomMenuUtil;
import com.github.obelieve.utils.ImagePreviewUtil;
import com.github.obelieve.utils.TelephoneUtil;
import com.github.obelieve.utils.others.TranslationState;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.dialog.CommonDialog;
import com.zxy.frame.net.ApiBaseResponse;
import com.zxy.frame.net.ApiBaseSubscribe;
import com.zxy.frame.net.ApiService;
import com.zxy.frame.net.ApiServiceException;
import com.zxy.frame.utils.SystemUtil;
import com.zxy.frame.utils.ToastUtil;
import com.zxy.frame.utils.image.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * @author KCrason
 * @date 2018/4/27
 */
public class UpdatesAdapter extends BaseRecyclerViewAdapter<SquareListsEntity.PostListBean>
        implements AppDataUtil.OnItemClickPopupMenuListener {

    private Activity mContext;

    private String mTag;

    private DelPostDialog mDelPostDialog;


    public UpdatesAdapter(Activity context, String tag) {
        super(context);
        this.mContext = context;
        this.mTag = tag;
    }

    private void checkListAll(List<SquareListsEntity.PostListBean> postListBeans) {
        if (postListBeans != null) {
            for (SquareListsEntity.PostListBean bean : postListBeans) {
                bean.setShowCheckAll(AppDataUtil.calculateShowCheckAllText(bean.getContent()));
            }
        }
    }

    @Override
    public BaseDataHolder<SquareListsEntity.PostListBean> getDataHolder() {
        if (mDataHolder == null) {
            mDataHolder = new BaseDataHolder<SquareListsEntity.PostListBean>(this) {
                @Override
                public void setList(List<SquareListsEntity.PostListBean> list) {
                    checkListAll(list);
                    super.setList(list);
                }
            };
        }
        return mDataHolder;
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_ONLY_WORD) {
            return new OnlyWordViewHolder(parent, R.layout.item_recycler_update_only_word);
        } else if (viewType == SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_WORD_AND_IMAGES) {
            return new WordAndImagesViewHolder(parent, R.layout.item_recycler_update_word_and_images);
        } else if (viewType == SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_ONE_IMAGE) {
            return new OneImageViewHolder(parent, R.layout.item_recycler_update_one_image);
        }
        return null;
    }

    @Override
    public void loadViewHolder(BaseViewHolder holder, int position) {
        if (holder != null && getDataHolder().getList() != null && position < getDataHolder().getList().size()) {
            SquareListsEntity.PostListBean postListBean = getDataHolder().getList().get(position);
            makeUserBaseData((BaseUpdateViewHolder) holder, postListBean, position);
            holder.bind(getDataHolder().getList().get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list = getDataHolder().getList().get(position).getMedia().getMedia_list();
        if (media_list == null || media_list.size() == 0) {
            return SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_ONLY_WORD;
        } else if (media_list.size() == 1) {
            return SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_ONE_IMAGE;
        } else {
            return SquareListsEntity.PostListBean.ViewType.UPDATE_TYPE_WORD_AND_IMAGES;
        }
    }

    @Override
    public void onItemClickCopy(int position) {
        Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickHideTranslation(int position) {
//        if (mPostListBeans != null && position < mPostListBeans.size()) {
//            mPostListBeans.get(position).setTranslationState(TranslationState.START);
//            notifyTargetItemView(position, TranslationState.START, null);
//        }
    }


    @Override
    public void onItemClickCollection(int position) {
        Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
    }


    private void sendComment(Dialog dialog, SquareListsEntity.PostListBean postListBean, String content) {
//        ((BaseActivity) mContext).showLoading();
        ApiService.wrap(App.getServiceInterface().sendPostComment(postListBean.getPost_id(), content), String.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mContext) {
                    @Override
                    public void onError(ApiServiceException e) {
//                        ((BaseActivity) mContext).dismissLoading();
                        ToastUtil.show(e.message);
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<String> response, boolean isProcessed) {
//                        ((BaseActivity) mContext).dismissLoading();
                        dialog.dismiss();
                        postListBean.setPc_num(postListBean.getPc_num() + 1);
                        if (!isProcessed) {
                            ToastUtil.show("发布成功");
                        }
                        notifyDataSetChanged();
                    }
                });
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
        ApiServiceWrapper.wrap(App.getServiceInterface().zanPost(postListBean.getPost_id(), null), PraiseEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<PraiseEntity>>(mContext) {
                    @Override
                    public void onError(ApiServiceException e) {
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<PraiseEntity> response, boolean isProcessed) {

                    }
                });
    }

    private void makeUserBaseData(BaseUpdateViewHolder holder, SquareListsEntity.PostListBean postListBean, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.layoutContent.getLayoutParams();
        holder.txtContent.setText(postListBean.getContent());
        //正文没有内容时隐藏布局
        holder.txtContent.setVisibility(postListBean.getContent().length() == 0 ? View.GONE : View.VISIBLE);
        setContentShowState(holder, postListBean);
        holder.txtContent.setOnLongClickListener(v -> {
            AppDataUtil.showPopupMenu(mContext, this, position, v, TranslationState.START);
            return true;
        });
        holder.layoutContent.setOnClickListener(v -> {
            ActivityUtil.gotoUpdateDetailActivity(mContext, postListBean.getPost_id(), postListBean.getUser_id(), mTag);
            if (mItemClickCallback != null) {
                mItemClickCallback.onItemClick(holder.itemView, postListBean, position);
            }
        });
        holder.txtContent.setOnClickListener(v -> {
            ActivityUtil.gotoUpdateDetailActivity(mContext, postListBean.getPost_id(), postListBean.getUser_id(), mTag);
            if (mItemClickCallback != null) {
                mItemClickCallback.onItemClick(holder.itemView, postListBean, position);
            }
        });

        holder.txtUserName.setText(postListBean.getNickname());
        //todo 头像
        Glide.with(mContext).load(postListBean.getAvatar()).into(holder.imgAvatar);

        holder.txtPublishTime.setText(postListBean.getPost_time());
        holder.txtPublishTime.setOnClickListener(view -> {
            ActivityUtil.gotoPersonalPageActivity(mContext, postListBean.getUser_id());
        });
        //点击头像跳转个人主页
        holder.txtUserName.setOnClickListener(view -> {
            ActivityUtil.gotoPersonalPageActivity(mContext, postListBean.getUser_id());
        });
        holder.imgAvatar.setOnClickListener(view -> {
            ActivityUtil.gotoPersonalPageActivity(mContext, postListBean.getUser_id());
        });
        //是否置顶
        holder.txtStick.setVisibility(postListBean.getIs_top() == 1 ? View.VISIBLE : View.GONE);
        holder.tvCommentNum.setText(postListBean.getPc_num() + "");
        holder.tvLikeNum.setText(postListBean.getZan_num() + "");
        holder.tvLikeNum.setSelected(postListBean.getIs_zan() == 1);
        AppDataUtil.showZanComment(holder.tvLikeNum, postListBean.getZan_num());
        AppDataUtil.showZanComment(holder.tvCommentNum, postListBean.getPc_num());
        holder.cbLike.setChecked(postListBean.getIs_zan() == 1);
        holder.iv_dialog.setVisibility(View.VISIBLE);
        holder.iv_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteOrReport(postListBean.getUser_id(), postListBean.getPost_id(), "post");
            }
        });
        holder.layoutPraise.setOnClickListener(view -> {
            if (view.getContext() instanceof Activity) {
                if (AppDataUtil.isAlertShow((Activity) view.getContext())) {
                    return;
                }
            }
            if (TextUtils.isEmpty(SystemValue.token)) {
                ActivityUtil.gotoLoginActivity(mContext);
                return;
            }
            likeOrNoComment(holder.view_lottie_zan, position, postListBean);
        });
        holder.layoutComment.setOnClickListener(view -> {
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
                contentView.findViewById(R.id.btn_ok).setOnClickListener(view1 -> sendComment(bottomDialog, postListBean, editComment.getText().toString()));
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels;
                layoutParams.height = SystemUtil.dp2px(App.getContext(), 50);
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
        });
    }

    private void setTextState(BaseUpdateViewHolder holder, boolean isExpand) {
        if (isExpand) {
            holder.txtContent.setMaxLines(Integer.MAX_VALUE);
            holder.txtState.setText("收起");
        } else {
            holder.txtContent.setMaxLines(4);
            holder.txtState.setText("全文");
        }
    }

    private void setContentShowState(BaseUpdateViewHolder holder, SquareListsEntity.PostListBean postListBean) {
        //todo 查看全文
        if (postListBean.isShowCheckAll()) {
            holder.txtState.setVisibility(View.VISIBLE);
            setTextState(holder, postListBean.isExpanded());
            holder.txtState.setOnClickListener(v -> {
                if (postListBean.isExpanded()) {
                    postListBean.setExpanded(false);
                } else {
                    postListBean.setExpanded(true);
                }
                setTextState(holder, postListBean.isExpanded());
            });
        } else {
            holder.txtState.setVisibility(View.GONE);
            holder.txtContent.setMaxLines(Integer.MAX_VALUE);
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
        if (mDelPostDialog == null) {
            mDelPostDialog = new DelPostDialog(mContext);
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
//        if (mContext instanceof BaseActivity) {
//            ((BaseActivity) mContext).showLoading();
//        }
        ApiService.wrap(App.getServiceInterface().getReportType(), ReportTypeEntity.class)
                .subscribe(new ApiBaseSubscribe<ApiBaseResponse<ReportTypeEntity>>(mContext) {
                    @Override
                    public void onError(ApiServiceException e) {
//                        if (mContext instanceof BaseActivity) {
//                            ((BaseActivity) mContext).dismissLoading();
//                        }
                    }

                    @Override
                    public void onSuccess(ApiBaseResponse<ReportTypeEntity> response, boolean isProcessed) {
                        if (mContext == null)
                            return;
//                        if (mContext instanceof BaseActivity) {
//                            ((BaseActivity) mContext).dismissLoading();
//                        }
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
                                    ApiService.wrap(App.getServiceInterface().reportPost(p_id, ids[index2], p_type), String.class)
                                            .subscribe(new ApiBaseSubscribe<ApiBaseResponse<String>>(mContext) {
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

    public static class WordAndImagesViewHolder extends BaseUpdateViewHolder {

        public NineGridView nineGridView;
        private Activity mContext;

        public WordAndImagesViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            nineGridView = itemView.findViewById(R.id.nine_grid_view);
            mContext = (Activity) parent.getContext();
        }

        @Override
        public void bind(SquareListsEntity.PostListBean postListBean) {
            //图文
            nineGridView.setAdapter(new NineImageAdapter(mContext, postListBean.getMedia().getMedia_list()));
            nineGridView.setOnImageClickListener((position1, view) -> {
                List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list = postListBean.getMedia().getMedia_list();
                List<View> views = new ArrayList<>();
                List<String> imgUrls = new ArrayList<>();
                for (int i = 0; i < media_list.size(); i++) {
                    if (nineGridView.getImageViews() != null && nineGridView.getImageViews().size() >= media_list.size()) {
                        views.add(nineGridView.getImageViews().get(i));
                    }
                    if (media_list.get(i) != null) {
                        imgUrls.add(media_list.get(i).getOriginal());
                    }
                }
                ImagePreviewUtil.show(mContext, views, imgUrls, position1);
            });
        }
    }

    public static class OneImageViewHolder extends BaseUpdateViewHolder {

        public ImageView imageSingle;
        private Activity mContext;

        public OneImageViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            imageSingle = itemView.findViewById(R.id.image_single);
            mContext = (Activity) parent.getContext();
        }

        @Override
        public void bind(SquareListsEntity.PostListBean postListBean) {
            //单图片样式
            GlideUtil.loadImageAutoSize(mContext, postListBean.getMedia().getMedia_list().get(0).getThumbnail(), imageSingle, SystemUtil.dp2px(App.getContext(), 175));
            //单图点击事件
            imageSingle.setOnClickListener(view -> {
                List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> media_list = postListBean.getMedia().getMedia_list();
                List<View> views = new ArrayList<>();
                List<String> imgUrls = new ArrayList<>();
                views.add(view);
                for (int i = 0; i < media_list.size(); i++) {
                    if (media_list.get(i) != null) {
                        imgUrls.add(media_list.get(i).getOriginal());
                    }
                }
                ImagePreviewUtil.show(mContext, views, imgUrls, 0);
            });
        }
    }


    public static class OnlyWordViewHolder extends BaseUpdateViewHolder {

        public OnlyWordViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }
    }

    public static class BaseUpdateViewHolder extends BaseViewHolder<SquareListsEntity.PostListBean> {

        public TextView txtUserName;
        public ImageView imgAvatar;
        public TextView txtPublishTime;
        public TextView txtContent;
        public TextView txtState;
        public TextView txtStick;
        public TextView tvCommentNum;
        public TextView tvLikeNum;
        public ViewGroup layoutContent;
        public ViewGroup layoutPraise;
        public ViewGroup layoutPraiseAndComment;
        public ViewGroup layoutComment;
        public CheckBox cbLike;
        public ImageView iv_dialog;
        public LottieAnimationView view_lottie_zan;


        public BaseUpdateViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
            layoutPraise = itemView.findViewById(R.id.layout_praise);
            layoutPraiseAndComment = itemView.findViewById(R.id.layout_praise_and_comment);
            layoutComment = itemView.findViewById(R.id.layout_comment);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            layoutContent = itemView.findViewById(R.id.layout_content);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtPublishTime = itemView.findViewById(R.id.txt_publish_time);
            txtContent = itemView.findViewById(R.id.txt_content);
            txtState = itemView.findViewById(R.id.txt_state);
            txtStick = itemView.findViewById(R.id.txt_stick);
            tvCommentNum = itemView.findViewById(R.id.tv_comment_num);
            tvLikeNum = itemView.findViewById(R.id.tv_likenum);
            cbLike = itemView.findViewById(R.id.cb_like);
            iv_dialog = itemView.findViewById(R.id.iv_dialog);
            view_lottie_zan = itemView.findViewById(R.id.view_lottie_zan);
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
