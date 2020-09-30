package com.github.obelieve.community.adapter.viewholder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.github.obelieve.community.R;
import com.github.obelieve.community.bean.SquareListsEntity;
import com.github.obelieve.community.bean.UpdateDetailEntity;
import com.github.obelieve.thirdsdklib.ImagePreviewUtil;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.utils.SystemUtil;
import com.zxy.frame.utils.image.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin
 * on 2020/8/25
 */
public class UpdateDetailViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<UpdateDetailEntity> {

    @BindView(R.id.cb_content)
    ConvenientBanner cbContent;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    private Activity mActivity;

    public UpdateDetailViewHolder(ViewGroup parent) {
        super(parent, R.layout.viewholder_update_detail);
        mActivity = (Activity) parent.getContext();
        if (cbContent.getLayoutParams() != null) {
            cbContent.getLayoutParams().height = SystemUtil.screenWidth(mActivity);
            cbContent.setLayoutParams(cbContent.getLayoutParams());
        }
    }

    @Override
    public void bind(UpdateDetailEntity bean) {
        if (bean.getMedia() != null && bean.getMedia().getMedia_list() != null) {
            cbContent.setVisibility(View.VISIBLE);
            List<SquareListsEntity.PostListBean.MediaBean.MediaListBean> list = bean.getMedia().getMedia_list();
            cbContent.setPages(new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new Holder(itemView) {
                        ImageView ivContent;

                        @Override
                        protected void initView(View itemView) {
                            ivContent = itemView.findViewById(R.id.iv_content);
                        }

                        @Override
                        public void updateUI(Object data) {
                            if (data instanceof SquareListsEntity.PostListBean.MediaBean.MediaListBean)
                                GlideUtil.loadImage(mActivity, ((SquareListsEntity.PostListBean.MediaBean.MediaListBean) data).getOriginal(),
                                        ivContent, R.drawable.failed_to_load_2, R.drawable.failed_to_load_2);
                        }
                    };
                }

                @Override
                public int getLayoutId() {
                    return R.layout.view_find_banner_item;
                }
            }, bean.getMedia().getMedia_list())
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setPageIndicator(new int[]{R.drawable.bg_indicator_banner_circle_nor, R.drawable.bg_indicator_banner_circle_press})
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if (position >= 0 && position < list.size()) {
                                List<View> views = new ArrayList<>();
                                List<String> imgUrls = new ArrayList<>();
                                views.add(cbContent);
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i) != null) {
                                        imgUrls.add(list.get(i).getOriginal());
                                    }
                                }
                                ImagePreviewUtil.show(mActivity, views, imgUrls, position);
                            }
                        }
                    }).startTurning();
        } else {
            cbContent.setVisibility(View.GONE);
        }
        tvContent.setText(bean.getContent());
        tvCreateTime.setText(bean.getPost_time());
    }

    public void onStart() {
        cbContent.startTurning();
    }

    public void onStop() {
        cbContent.stopTurning();
    }
}
