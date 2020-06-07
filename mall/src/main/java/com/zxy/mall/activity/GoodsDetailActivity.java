package com.zxy.mall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.zxy.frame.adapter.BaseRecyclerViewAdapter;
import com.zxy.frame.base.BaseActivity;
import com.zxy.frame.utils.StatusBarUtil;
import com.zxy.frame.utils.image.GlideUtil;
import com.zxy.mall.R;
import com.zxy.mall.entity.FoodEntity;
import com.zxy.mall.entity.RatingEntity;
import com.zxy.mall.entity.mock.MockRepository;
import com.zxy.mall.utils.ShoppingCartManager;
import com.zxy.mall.view.header.GoodsDetailHeaderView;
import com.zxy.utility.SystemUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class GoodsDetailActivity extends BaseActivity {


    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_FOOD_NAME = "food_name";


    @BindView(R.id.view_goods_detail_header)
    GoodsDetailHeaderView mViewGoodsDetailHeader;
    @BindView(R.id.view_statusBar)
    View mViewStatusBar;
    @BindView(R.id.rl_left)
    RelativeLayout mRlLeft;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.tv_nav_title)
    TextView mTvNavTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.abl_content)
    AppBarLayout mAblContent;
    @BindView(R.id.rv_rating_category)
    RecyclerView mRvRatingCategory;
    @BindView(R.id.cb_select_content)
    CheckBox mCbSelectContent;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    RatingCategoryAdapter mRatingCategoryAdapter;
    GoodsDetailAdapter mGoodsDetailAdapter;

    ShoppingCartListener mShoppingCartListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mNeedInsetStatusBar = false;
        mLightStatusBar = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_goods_detail;
    }


    public static void start(Activity activity, String name, String foodName) {
        Intent intent = new Intent(activity, GoodsDetailActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_FOOD_NAME, foodName);
        activity.startActivity(intent);
    }

    @Override
    protected void initCreateAfterView(Bundle savedInstanceState) {
        mShoppingCartListener = new ShoppingCartListener();
        ShoppingCartManager.getInstance().addListener(mShoppingCartListener);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String foodName = getIntent().getStringExtra(EXTRA_FOOD_NAME);
        FoodEntity foodEntity = MockRepository.getFoodEntity(name, foodName);
        mViewStatusBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SystemUtil.getStatusBarHeight()));
        mTvNavTitle.setText(foodEntity.getName());
        mViewGoodsDetailHeader.loadData(foodEntity);
        mRatingCategoryAdapter = new RatingCategoryAdapter(this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(RecyclerView.HORIZONTAL);
        mRvRatingCategory.setLayoutManager(lm);
        mRvRatingCategory.setAdapter(mRatingCategoryAdapter);
        mRatingCategoryAdapter.setItemClickCallback(new BaseRecyclerViewAdapter.OnItemClickCallback<RatingCategoryEntity>() {
            @Override
            public void onItemClick(View view, RatingCategoryEntity entity, int position) {
                mGoodsDetailAdapter.getDataHolder().setList(entity.getEntityList());
            }
        });
        if (foodEntity.getRatings() != null) {
            RatingCategoryEntity all = new RatingCategoryEntity();
            all.setSelected(true);
            all.setType(0);
            all.setName("全部");
            all.setEntityList(new ArrayList<>());
            RatingCategoryEntity satisfaction = new RatingCategoryEntity();
            satisfaction.setType(0);
            satisfaction.setName("满意");
            satisfaction.setEntityList(new ArrayList<>());
            RatingCategoryEntity unsatisfaction = new RatingCategoryEntity();
            unsatisfaction.setType(1);
            unsatisfaction.setName("不满意");
            unsatisfaction.setEntityList(new ArrayList<>());
            for (RatingEntity ratingEntity : foodEntity.getRatings()) {
                if (ratingEntity.getRateType() == 0) {
                    all.getEntityList().add(ratingEntity);
                    satisfaction.getEntityList().add(ratingEntity);
                } else {
                    all.getEntityList().add(ratingEntity);
                    unsatisfaction.getEntityList().add(ratingEntity);
                }
            }
            mRatingCategoryAdapter.getDataHolder().setList(Arrays.asList(all, satisfaction, unsatisfaction));
        }
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mGoodsDetailAdapter = new GoodsDetailAdapter(this);
        mRvContent.setAdapter(mGoodsDetailAdapter);
        mGoodsDetailAdapter.getDataHolder().setList(foodEntity.getRatings());

        mAblContent.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int finalValue = SystemUtil.dp2px(25);
                if (Math.abs(verticalOffset) > finalValue) {
                    StatusBarUtil.setWindowLightStatusBar(mActivity,true);
                    StatusBarUtil.setStatusBarColor(mActivity, getStatusBarColor());
                } else {
                    StatusBarUtil.setWindowLightStatusBar(mActivity,false);
                    StatusBarUtil.setStatusBarTranslucentStatus(mActivity);

                }
                int bgColor = 0;
                if (Math.abs(verticalOffset) > finalValue) {
                    setToolbarBg(true);
                    bgColor = Color.argb(255, 255, 255, 255);
                } else if (Math.abs(verticalOffset) == 0) {
                    setToolbarBg(false);
                    bgColor = Color.argb(0, 0, 0, 0);
                } else {
                    int aColor = (int) ((Math.abs(verticalOffset) / (finalValue * 1.0f) * 255) * 0.8f);
                    if (aColor > 255 * 0.7f) {
                        setToolbarBg(true);
                    } else {
                        setToolbarBg(false);
                    }
                    bgColor = Color.argb(aColor, 255, 255, 255);
                }
                mToolbar.setBackgroundColor(bgColor);
            }
        });
    }

    private void setToolbarBg(boolean dark) {
        if (!dark) {
            mIvLeft.setImageResource(R.drawable.ic_arrow_left_white);
            mTvNavTitle.setTextColor(getResources().getColor(R.color.colorWhite));
            mTvNavTitle.setVisibility(View.GONE);
        } else {
            mIvLeft.setImageResource(R.drawable.ic_arrow_left_black);
            mTvNavTitle.setTextColor(getResources().getColor(R.color.color_07111b));
            mTvNavTitle.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.rl_left)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShoppingCartManager.getInstance().addListener(mShoppingCartListener);
    }

    public class ShoppingCartListener implements ShoppingCartManager.Listener{

        @Override
        public void onGoodsNotifyChanged(String id, String name, float price, int count,int maxCount) {
            mViewGoodsDetailHeader.setBuyNum(id,count,maxCount);
        }

        @Override
        public void onClearAll(List<String> list) {
            mViewGoodsDetailHeader.setBuyEmpty();
        }
    }

    public static class RatingCategoryEntity {

        private boolean selected;
        private int type;//0 全部/满意 1 不满意
        private String mName;
        private List<RatingEntity> mEntityList;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public List<RatingEntity> getEntityList() {
            return mEntityList;
        }

        public void setEntityList(List<RatingEntity> entityList) {
            mEntityList = entityList;
        }
    }

    public static class RatingCategoryAdapter extends BaseRecyclerViewAdapter<RatingCategoryEntity> {

        private int mCurPosition = -1;

        public RatingCategoryAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RCViewHolder(parent, R.layout.viewholder_rating_category);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            if (holder instanceof RCViewHolder) {
                RatingCategoryEntity entity = getDataHolder().getList().get(position);
                boolean selected = entity.isSelected();
                if (selected) {
                    mCurPosition = position;
                }
                ((RCViewHolder) holder).mTvName.setSelected(selected);
                ((RCViewHolder) holder).mTvNum.setSelected(selected);
                ((RCViewHolder) holder).mLlContent.setSelected(selected);
                if (entity.getType() == 0) {
                    ((RCViewHolder) holder).mLlContent.setBackgroundResource(R.drawable.sel_3300a0dc_00a0dc);
                } else {
                    ((RCViewHolder) holder).mLlContent.setBackgroundResource(R.drawable.sel_3393999f_00a0dc);
                }
                ((RCViewHolder) holder).mTvName.setText(entity.getName());
                ((RCViewHolder) holder).mTvNum.setText("" + (entity.getEntityList() != null ? entity.getEntityList().size() : 0));
                ((RCViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurPosition != position) {
                            if (mCurPosition != -1) {
                                getDataHolder().getList().get(mCurPosition).setSelected(false);
                            }
                            entity.setSelected(true);
                            if (mItemClickCallback != null) {
                                mItemClickCallback.onItemClick(v, entity, position);
                            }
                            mCurPosition = position;
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }

        public class RCViewHolder extends BaseViewHolder {

            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.tv_num)
            TextView mTvNum;
            @BindView(R.id.ll_content)
            LinearLayout mLlContent;

            public RCViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class GoodsDetailAdapter extends BaseRecyclerViewAdapter<RatingEntity> {


        public GoodsDetailAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
            return new RatingViewHolder(parent, R.layout.viewholder_goods_detail_rating);
        }

        @Override
        public void loadViewHolder(BaseViewHolder holder, int position) {
            holder.bind(getDataHolder().getList().get(position));
        }


        public class RatingViewHolder extends BaseViewHolder<RatingEntity> {

            @BindView(R.id.civ_head)
            CircleImageView mCivHead;
            @BindView(R.id.tv_name)
            TextView mTvName;
            @BindView(R.id.rb_score)
            RatingBar mRbScore;
            @BindView(R.id.tv_delivery_time)
            TextView mTvDeliveryTime;
            @BindView(R.id.tv_rate_time)
            TextView mTvRateTime;
            @BindView(R.id.tv_content)
            TextView mTvContent;
            @BindView(R.id.iv_like)
            ImageView mIvLike;
            @BindView(R.id.cl_content)
            ConstraintLayout mClContent;

            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            public RatingViewHolder(ViewGroup parent, int layoutId) {
                super(parent, layoutId);
            }

            @Override
            public void bind(RatingEntity bean) {
                GlideUtil.loadImage(itemView.getContext(), bean.getAvatar(), mCivHead);
                mTvName.setText(bean.getUsername());
                mRbScore.setRating(bean.getScore());
                mIvLike.setSelected(bean.getRateType() == 1);
                mTvDeliveryTime.setText(bean.getDeliveryTime() + "分钟送达");
                mTvRateTime.setText(mFormat.format(new Date(bean.getRateTime())));
                mTvContent.setText(bean.getText());
                if (TextUtils.isEmpty(bean.getText())) {
                    mTvContent.setVisibility(View.GONE);
                } else {
                    mTvContent.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
